package com.qxlabai.presentation.connection

import android.util.Log
import com.qxlabai.domain.collectors.ContactsCollector
import com.qxlabai.domain.collectors.ContactsRepository
import com.qxlabai.domain.entity.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.roster.PresenceEventListener
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.roster.RosterEntry
import org.jivesoftware.smack.roster.RosterListener
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jxmpp.jid.BareJid
import org.jxmpp.jid.FullJid
import org.jxmpp.jid.Jid
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject

class RosterManagerImpl @Inject constructor(
    private val contactsCollector: ContactsCollector,
    private val contactsRepository: ContactsRepository
) : RosterManager{

   companion object {
       private val TAG = this::class.java.simpleName
   }
    private val scope = CoroutineScope(SupervisorJob())

    private lateinit var roster: Roster

    private var rosterListener: RosterListener? = null

    private var presenceEventListener: PresenceEventListener? = null

    init {
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all)
        Roster.setRosterLoadedAtLoginDefault(true)
    }

    override suspend fun initialize(connection: XMPPTCPConnection) {
        roster = Roster.getInstanceFor(connection)

        Log.d(TAG, "Roster entries: ${roster.entries}")

        createNewContacts(roster.entries)

        roster.addRosterListener()

        roster.addPresenceEventListener()

        scope.launch {
            contactsCollector.collectShouldAddToRosterContacts { addToRoster(it) }
        }
    }

    /**
     * Create contacts if entry jid is of type EntityBareJid and does not exist already
     */
    private suspend fun createNewContacts(rosterEntries: Set<RosterEntry>) {
        val contacts = contactsRepository.getContactsStream().first()

        val newContacts = rosterEntries
            .filter { it.jid.isEntityBareJid }
            .filter { entry ->
                contacts.none { contact ->
                    contact.jid == entry.jid.asBareJid().toString()
                }
            }
            .map(RosterEntry::asExternalModel)

        contactsRepository.updateContacts(newContacts)
    }

    private fun addToRoster(contacts: List<Contact>) {
        contacts.forEach { createEntry(it.jid) }
    }

    private fun createEntry(jid: String) {
        // TODO: check if createItemAndRequestSubscription is the right way to create entry
        // createItemAndRequestSubscription should be supported by XMPP server
        roster.createItemAndRequestSubscription(JidCreate.bareFrom(jid), null, null)
    }

    private fun Roster.addRosterListener() {
        rosterListener = object : RosterListener {
            override fun entriesAdded(addresses: MutableCollection<Jid>?) {
                Log.d(TAG, "EntriesAdded $addresses")
            }

            override fun entriesUpdated(addresses: MutableCollection<Jid>?) {
                Log.d(TAG, "entriesUpdated $addresses")
            }

            override fun entriesDeleted(addresses: MutableCollection<Jid>?) {
                Log.d(TAG, "entriesDeleted $addresses")
            }

            override fun presenceChanged(presence: Presence?) {
                Log.d(TAG, "presenceChanged $presence")
            }
        }

        addRosterListener(rosterListener)
    }

    private fun Roster.addPresenceEventListener() {
        presenceEventListener = object : PresenceEventListener {
            override fun presenceAvailable(address: FullJid?, availablePresence: Presence?) {
                Log.d(TAG, "presenceAvailable $address $availablePresence")
            }

            override fun presenceUnavailable(address: FullJid?, presence: Presence?) {
                Log.d(TAG, "presenceUnavailable $address $presence")
            }

            override fun presenceError(address: Jid?, errorPresence: Presence?) {
                Log.d(TAG, "presenceError $address $errorPresence")
            }

            override fun presenceSubscribed(address: BareJid?, subscribedPresence: Presence?) {
                Log.d(TAG, "presenceSubscribed $address $subscribedPresence")
            }

            override fun presenceUnsubscribed(address: BareJid?, unsubscribedPresence: Presence?) {
                Log.d(TAG, "presenceError $address $unsubscribedPresence")
            }
        }

        addPresenceEventListener(presenceEventListener)
    }

    override fun onCleared() {
        scope.cancel()
        if (this::roster.isInitialized) {
            roster.removeRosterListener(rosterListener)
            roster.removePresenceEventListener(presenceEventListener)
        }
    }
}