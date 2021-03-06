// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

package com.zimbra.cs.imap;

import java.io.Serializable;

/**
 * @author zimbra
 *
 */
public class Hacker implements Serializable {

    private static final long serialVersionUID = 8550345789897309061L;
    private String name;

    public Hacker(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
