package com.nikhil.xmpp_test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";

        final String user_name = "7";
        final String password = "+912020202020";
//    final String user_name = "5";
//    final String password = "+917777777777";

    final String host = "192.168.245.182";
    final int port = 5222;
    final String service = "qa_donna_app";

    String smack_user_name = null;

    // user_id 5 , has skills also
    // password +917777777777
    // Log in as 5@qa_donna_app/Smack
    // qa_donna_app

    //user_id 7
    // pass +912020202020

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _initialize();
    }

    private void _initialize() {
        new XMPPConAsync().execute();
    }

    class XMPPConAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new GetStatusAsync().execute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //====================================
            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

            DomainBareJid serviceName = null;
            try {
                serviceName = JidCreate.domainBareFrom(service);
            } catch (XmppStringprepException e) {
                e.getCausingString();
            }

            config.setServiceName(serviceName);
            config.setHost(host);
            config.setPort(port);

            config.setResource("test");
            config.setDebuggerEnabled(true);

            config.setUsernameAndPassword(user_name, password);
            Log.d(TAG, " [username] " + user_name);
            Log.d(TAG, " [password] " + password);
            config.setCompressionEnabled(false);
            //====================================
            // XMPPConnection connection = new XMPPTCPConnection(config.build());

            // XMPPConnection object
            AbstractXMPPConnection conn2 = null;
            try {

                //====================================
//                conn2 = new XMPPTCPConnection("5", "+917777777777", "qa_donna_app");
//                conn2 = new XMPPTCPConnection(config.build());
                // Create a connectio
                conn2 = new XMPPTCPConnection(config.build());

                Log.d(TAG, "doInBackground [service] " + conn2.getServiceName());
                Log.d(TAG, "doInBackground [port] " + conn2.getPort());

                XMPPConSingleton xmppConSingleton = XMPPConSingleton.getInstance();

                conn2.connect();

                if (conn2.isConnected()) {

                    try {

//                        conn2.login(user_name, password);
                        conn2.login();
                        xmppConSingleton.setConnection(conn2);
                        Log.d(TAG, "doInBackground [connected to]: " + conn2.getHost() + "/@" + conn2.getServiceName());
                        Log.d(TAG, "doInBackground [Logged in as]: " + conn2.getUser());

                        smack_user_name = String.valueOf(conn2.getUser());

                    } catch (NullPointerException e) {
                        e.getMessage();
                        e.printStackTrace();
                        Log.d(TAG, "doInBackground [Failed login]");
                    }
                } else {
                    Log.d(TAG, "doInBackground [failed connecting]");
                }
                //====================================

            } catch (SmackException | InterruptedException | XMPPException | IOException e) {
                e.printStackTrace();
                e.getMessage();
            }

            // Create a new presence. Pass in false to indicate we're unavailable._
            Presence presence = new Presence(Presence.Type.available);
            presence.setStatus("Gone fishing");
            // Send the packet (assume we have an XMPPConnection instance called "con").
            XMPPConSingleton xmppConSingleton = XMPPConSingleton.getInstance();
            try {
                //=================================
                if (xmppConSingleton.getConnection() != null) {
                    xmppConSingleton.getConnection().sendStanza(presence);
                    Log.d(TAG, "doInBackground pre : " + presence.getStatus());
                    Log.d(TAG, "doInBackground [sendStanza] & [singleton not null]");
                }
                //=================================
            } catch (SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class GetStatusAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            XMPPConSingleton xmppConSingleton = XMPPConSingleton.getInstance();
            if (xmppConSingleton.getConnection() != null) {

                Roster roster = Roster.getInstanceFor(xmppConSingleton.getConnection());
                Log.e(TAG, "doInBackground [roster count]: " + roster.getGroupCount());


              /*  Collection<RosterEntry> entries = roster.getEntries();
                for (RosterEntry entry : entries) {
                    System.out.println(entry);
                }*/

            /*    DomainBareJid user_id = null;
                try {

                    user_id = JidCreate.domainBareFrom("7@qa_donna_app/Smack");
                    Presence presence = roster.getPresence(user_id);

                    Log.e(TAG, "doInBackground [status] : " + presence.getStatus());

                } catch (XmppStringprepException e) {
                    e.getCausingString();
                }*/

            } else {
                System.out.println("Instance = null");
            }

            return null;
        }
    }
}
