// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.cs.servlet.continuation;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletRequest;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationListener;
import org.eclipse.jetty.continuation.ContinuationSupport;

import com.zimbra.common.util.ZimbraLog;

/**
 * ContinuationListener implementation to handle internal details of when and when not to attempt resume
 * Application code which implements timeout + explicit resume should do so via this class
 *
 */
public class ResumeContinuationListener implements ContinuationListener {

    private Continuation continuation;
    private AtomicBoolean readyToResume;

    public ResumeContinuationListener(Continuation continuation) {
        this.continuation = continuation;
        this.readyToResume = new AtomicBoolean(false);
        continuation.addContinuationListener(this);
    }

    public static ResumeContinuationListener getResumableContinuation(ServletRequest request) {
        return new ResumeContinuationListener(ContinuationSupport.getContinuation(request));
    }

    @Override
    public void onComplete(Continuation theContinuation) {
        ZimbraLog.session.trace("ResumeContinuationListener.onTimeout");
        readyToResume.set(false);
    }

    @Override
    public void onTimeout(Continuation theContinuation) {
        ZimbraLog.session.trace("ResumeContinuationListener.onTimeout");
        readyToResume.set(false);
    }

    /**
     * Attempt to resume continuation if it is currently suspended.
     */
    public synchronized void resumeIfSuspended() {
        if (readyToResume.compareAndSet(true, false)) {
            try {
                ZimbraLog.session.trace("ResumeContinuationListener.resumeIfSuspended RESUMING");
                continuation.resume();
            } catch (IllegalStateException ise) {
                if (!(continuation.isExpired() || continuation.isResumed())) {
                    //narrow race here; timeout could occur just after compareAndSet
                    //not a problem as long as it is expired or resumed
                    throw ise;
                } else {
                    ZimbraLog.session.debug(
                            "ignoring IllegalStateException during resume; already resumed/expired", ise);
                }
            }
        }
    }

    /**
     * Put the continuation into suspended state.
     * @param timeout
     */
    public synchronized void suspendAndUndispatch(long timeout) {
        readyToResume.set(true);
        continuation.setTimeout(timeout);
        continuation.suspend();
        continuation.undispatch();
    }

    public Continuation getContinuation() {
        return continuation;
    }

}
