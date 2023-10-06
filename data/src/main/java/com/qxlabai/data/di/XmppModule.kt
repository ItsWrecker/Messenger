package com.qxlabai.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.qxlabai.data.datastore.entiry.AppLock
import com.qxlabai.data.datastore.serializer.AppLockSerializer
import com.qxlabai.data.datastore.serializer.CredentialSerializer
import com.qxlabai.domain.interactors.xmpp.entity.Credentials
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.DomainBareJid
import org.jxmpp.jid.impl.JidCreate
import java.io.File
import java.net.InetAddress
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object XmppModule {

    private const val TIMEOUT = 45_000
    private const val serverAddress = "conversations.im"
    private val xmppServiceDomain: DomainBareJid = JidCreate.domainBareFrom(serverAddress)
    private const val port = 5222

    @Provides
    @Singleton
    fun provideXmppConnection(): XMPPTCPConnectionConfiguration {
        return XMPPTCPConnectionConfiguration.builder()
            .setConnectTimeout(XMPPTCPConnectionConfiguration.DEFAULT_CONNECT_TIMEOUT)
            .setXmppDomain(xmppServiceDomain)
            //.setHostAddress(InetAddress.getByName(serverAddress))

            .setPort(port)
            .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
            .build()
    }

    @Provides
    @Singleton
    fun provideConnection(
        connectionConfiguration: XMPPTCPConnectionConfiguration
    ): AbstractXMPPConnection {
        return XMPPTCPConnection(connectionConfiguration)
    }

    @Provides
    @Singleton
    fun provideAccountManager(
        connection: AbstractXMPPConnection
    ): AccountManager = AccountManager.getInstance(connection)

    @Provides
    @Singleton
    fun provideChatManger(
        connection: AbstractXMPPConnection
    ): ChatManager = ChatManager.getInstanceFor(connection)

    @Provides
    @Singleton
    fun provideAppDataStore(
        context: Context
    ): DataStore<AppLock> {
        return DataStoreFactory.create(
            serializer = AppLockSerializer,
            null,
            produceFile = { File(context.cacheDir, "messenger.json") }
        )
    }

    @Provides
    @Singleton
    fun provideCredentialsStore(
        context: Context
    ): DataStore<Credentials> {
        return DataStoreFactory.create(
            serializer = CredentialSerializer,
            null,
            produceFile = { File(context.cacheDir, "credentials.json") }
        )
    }

}