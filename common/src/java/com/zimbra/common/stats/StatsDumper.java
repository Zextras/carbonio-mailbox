// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.common.stats;

import com.zimbra.common.util.FileUtil;
import com.zimbra.common.util.ZimbraLog;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Writes data to a file at a scheduled interval. Data and headers are retrieved from a {@link
 * StatsDumperDataSource}.
 */
public class StatsDumper implements Callable<Void> {

  public static final int SYSLOG_ELIDING_THRESHOLD = 800;
  private static final File STATS_DIR = new File("/opt/zextras/zmstat");
  private static final ThreadGroup statsGroup = new ThreadGroup("ZimbraPerf Stats");

  private final StatsDumperDataSource mDataSource;
  private Calendar mLastRollover = Calendar.getInstance();

  private StatsDumper(StatsDumperDataSource dataSource) {
    mDataSource = dataSource;
  }

  /**
   * Schedules a new stats task.
   *
   * @param dataSource the data source
   * @param intervalMillis interval between writes. The first write is delayed by this interval.
   */
  public static void schedule(final StatsDumperDataSource dataSource, final long intervalMillis) {
    // Stop using TaskScheduler (bug 22978)
    final StatsDumper dumper = new StatsDumper(dataSource);
    final StatsDumperTask r = new StatsDumperTask(intervalMillis, dataSource, dumper);
    new Thread(statsGroup, r, dataSource.getFilename()).start();
  }

  private File getFile() throws IOException {
    FileUtil.ensureDirExists(STATS_DIR);
    return new File(STATS_DIR, mDataSource.getFilename());
  }

  /** Return the archive directory, based on {@link #mLastRollover}. */
  private File getArchiveDir() throws IOException {
    String dirName = String.format("%1$tY-%1$tm-%1$td", mLastRollover);
    File dir = new File(STATS_DIR, dirName);
    FileUtil.ensureDirExists(dir);
    return dir;
  }

  private void rollover() throws IOException {
    File sourceFile = getFile();
    File archiveDir = getArchiveDir();
    File archiveFile = new File(archiveDir, mDataSource.getFilename() + ".gz");
    FileUtil.compress(sourceFile, archiveFile, true);
    Files.delete(Path.of(sourceFile.getPath()));
  }

  /**
   * Gets the latest data from the data source and writes it to the file. If this is a new file,
   * writes the header first.
   */
  public Void call() throws Exception {
    Collection<String> lines = mDataSource.getDataLines();
    if (lines == null || lines.size() == 0) {
      return null;
    }

    // Assemble data lines
    StringBuilder buf = new StringBuilder();
    String timestamp = StatUtil.getTimestampString();
    for (String line : lines) {
      if (mDataSource.hasTimestampColumn()) {
        buf.append(timestamp).append(",");
      }
      buf.append(line).append("\n");
    }

    // Rollover is necessary
    File file = getFile();
    Calendar now = Calendar.getInstance();
    if (file.exists() && now.get(Calendar.DAY_OF_YEAR) != mLastRollover.get(Calendar.DAY_OF_YEAR)) {
      rollover();
      mLastRollover = now;
    }

    // Determine if header needs to be written
    boolean writeHeader = !file.exists();
    String header = mDataSource.getHeader();
    try (FileWriter writer = new FileWriter(file, true)) {
      if (writeHeader) {
        if (mDataSource.hasTimestampColumn()) {
          writer.write("timestamp,");
        }
        writer.write(header);
        writer.write("\n");
      }
      // Write data and close
      writer.write(buf.toString());
    }

    for (String line : lines) {
      String logLine =
          mDataSource.getFilename()
              + ": "
              + (mDataSource.hasTimestampColumn() ? "timestamp," : "")
              + header
              + ":: "
              + timestamp
              + ","
              + line;
      if (logLine.length() <= SYSLOG_ELIDING_THRESHOLD) {
        ZimbraLog.slogger.info(logLine);
      } else {
        StringBuilder b = new StringBuilder(logLine);
        String lastUuid = null;
        do {
          String sub = b.substring(0, SYSLOG_ELIDING_THRESHOLD);
          b.delete(0, SYSLOG_ELIDING_THRESHOLD);
          if (lastUuid != null) {
            sub = ":::" + lastUuid + ":::" + sub;
          }
          lastUuid = UUID.randomUUID().toString();
          ZimbraLog.slogger.info(sub + ":::" + lastUuid + ":::");
        } while (b.length() > SYSLOG_ELIDING_THRESHOLD);
        ZimbraLog.slogger.info(":::" + lastUuid + ":::" + b);
      }
    }
    return null;
  }
}
