package com.example.Rock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    public static final String LOGTAG = "rockkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk   :   ";

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOGTAG,"user clicked on login");

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        XMPPTCPConnectionConfiguration connectionConfiguration = null;



                        try {
                            connectionConfiguration  = XMPPTCPConnectionConfiguration.builder()
                                    .setUsernameAndPassword("zabeeh","18150")
                                //    .setXmppDomain("localhost")
                                    .setXmppDomain(JidCreate.domainBareFrom("localhost"))
                                    .setHostAddress(InetAddress.getByName("192.168.0.103"))
                                    .setKeystoreType(null)
                                    .setResource("LoginActivity")
                              //      .setKeystorePath(String.valueOf(path))
                                    .setPort(5180)
                                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                                    .setCompressionEnabled(true)
                                    .build();

                        } catch (XmppStringprepException | UnknownHostException e) {
                            e.printStackTrace();
                        }

                        AbstractXMPPConnection connection = null;
                        connection = new XMPPTCPConnection(connectionConfiguration);

                        connection.addConnectionListener(new ConnectionListener() {


                            @Override
                            public void connected(XMPPConnection connection) {
                                Log.d(LOGTAG,"connectted");
                            }

                            @Override
                            public void authenticated(XMPPConnection connection, boolean resumed) {
                                Log.d(LOGTAG,"authenticated");
                            }

                            @Override
                            public void connectionClosed() {
                                Log.d(LOGTAG,"connection closed");
                            }

                            @Override
                            public void connectionClosedOnError(Exception e) {
                                Log.d(LOGTAG,"connection closed due to eroor");
                            }
                        });

                        try {
                            connection.connect().login();
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        } catch (SmackException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                };

                Thread connectionThread = new Thread(runnable);
                connectionThread.start();

            }
        });



    }


}