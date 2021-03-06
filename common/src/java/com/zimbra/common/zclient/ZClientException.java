// SPDX-FileCopyrightText: 2022 Synacor, Inc.
// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: GPL-2.0-only

/*
 * Created on Jun 1, 2004
 *
 */
package com.zimbra.common.zclient;

import com.zimbra.common.service.ServiceException;

/**
 * @author schemers
 *
 */
public class ZClientException extends ServiceException {

    private static final long serialVersionUID = -728413545620278749L;

    public static final String CLIENT_ERROR       = "zclient.CLIENT_ERROR";
    public static final String IO_ERROR           = "zclient.IO_ERROR";
    public static final String UPLOAD_SIZE_LIMIT_EXCEEDED = "zclient.UPLOAD_SIZE_LIMIT_EXCEEDED";
    public static final String UPLOAD_FAILED = "zclient.UPLOAD_FAILED";
    public static final String ZIMBRA_SHARE_PARSE_ERROR = "zclient.ZIMBRA_SHARE_PARSE_ERROR";

    private ZClientException(String message, String code, boolean isReceiversFault) {
        super(message, code, isReceiversFault);
    }

    private ZClientException(String message, String code, boolean isReceiversFault, Throwable cause) {
        super(message, code, isReceiversFault, cause);
    }

    public static ZClientException IO_ERROR(String msg, Throwable cause) {
        return new ZClientException(msg, IO_ERROR, SENDERS_FAULT, cause);
    }

    public static ZClientException CLIENT_ERROR(String msg, Throwable cause) {
        return new ZClientException(msg, CLIENT_ERROR, SENDERS_FAULT, cause);
    }

    public static ZClientException UPLOAD_SIZE_LIMIT_EXCEEDED(String msg, Throwable cause) {
        return new ZClientUploadSizeLimitExceededException(msg, cause);
    }

    public static ZClientException UPLOAD_FAILED(String msg, Throwable cause) {
        return new ZClientException(msg, UPLOAD_FAILED, SENDERS_FAULT, cause);
    }

    public static ZClientException ZIMBRA_SHARE_PARSE_ERROR(String msg, Throwable cause) {
        return new ZClientException(msg, ZIMBRA_SHARE_PARSE_ERROR, SENDERS_FAULT, cause);
    }

    public static class ZClientNoSuchItemException extends ZClientException {
        private static final long serialVersionUID = 6322647788472338821L;

        public ZClientNoSuchItemException(String msg, Throwable cause) {
            super(msg, CLIENT_ERROR, SENDERS_FAULT, cause);
        }

        public ZClientNoSuchItemException(int id, Throwable cause) {
            super("no such item: "+ id, CLIENT_ERROR, SENDERS_FAULT, cause);
        }

        public ZClientNoSuchItemException(int id) {
            this(id, null);
        }
    }

    public static class ZClientUploadSizeLimitExceededException extends ZClientException {
        private static final long serialVersionUID = 1728995252512138761L;
        public ZClientUploadSizeLimitExceededException(String msg, Throwable cause) {
            super(msg, UPLOAD_SIZE_LIMIT_EXCEEDED, SENDERS_FAULT, cause);
        }
    }

    public static class ZClientNoSuchMsgException extends ZClientNoSuchItemException {
        private static final long serialVersionUID = 7916158633910054737L;

        public ZClientNoSuchMsgException(int id, Throwable cause) {
            super("no such message: "+ id, cause);
        }
    }

    public static class ZClientNoSuchContactException extends ZClientNoSuchItemException {
        private static final long serialVersionUID = -6622409008893327197L;

        public ZClientNoSuchContactException(int id, Throwable cause) {
            super("no such contact: "+ id, cause);
        }
    }

    public static ZClientException NO_SUCH_ITEM(int id) {
        return new ZClientNoSuchItemException(id);
    }

    public static ZClientException NO_SUCH_CONTACT(int id, Throwable cause) {
        return new ZClientNoSuchContactException(id, cause);
    }

    public static ZClientException NO_SUCH_MSG(int id, Throwable cause) {
        return new ZClientNoSuchMsgException(id, cause);
    }
}
