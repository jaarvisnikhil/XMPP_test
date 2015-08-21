package com.nikhil.xmpp_test;

import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by nikhil on 19-08-2015.
 */
public class XMPPConSingleton {

    private XMPPConnection connection;
    static XMPPConSingleton mObj = null;

    public XMPPConSingleton() {
    }

    public XMPPConnection getConnection() {
        return connection;
    }

    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
    }

    /**
     * This returns connection
     */
    public static XMPPConSingleton getInstance() {
        if (mObj == null) {
            mObj = new XMPPConSingleton();
        }
        return mObj;
    }
}
