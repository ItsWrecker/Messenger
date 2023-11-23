package com.qxlabai.messenger.service.xmpp.omemo

import org.jivesoftware.smackx.omemo.internal.OmemoDevice
import org.jivesoftware.smackx.omemo.trust.OmemoFingerprint
import org.jivesoftware.smackx.omemo.trust.OmemoTrustCallback
import org.jivesoftware.smackx.omemo.trust.TrustState


class EphemeralTrustCallback : OmemoTrustCallback {


    private val trustStates = HashMap<OmemoDevice, HashMap<OmemoFingerprint, TrustState>>()


    override fun getTrust(device: OmemoDevice?, fingerprint: OmemoFingerprint?): TrustState {
        val states = trustStates[device]

//        if (states != null) {
//            val trustState: TrustState? = states[fingerprint]
//            if (trustState != null) return trustState
//        }
        return TrustState.trusted
    }

    override fun setTrust(
        device: OmemoDevice?,
        fingerprint: OmemoFingerprint?,
        state: TrustState?
    ) {
        var states: HashMap<OmemoFingerprint, TrustState>? = trustStates[device]

        if (states == null) {
            states = HashMap()
            if (device != null)
                trustStates[device] = states
        }

        if (fingerprint != null && state != null)
            states[fingerprint] = state

    }
}