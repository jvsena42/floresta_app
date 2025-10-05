# floresta_app

![cover](/docs/Floresta_app_cover.png)

This repository contains the code for an Android Bitcoin Wallet powered by [Floresta](https://github.com/vinteumorg/floresta). It currently works 
only on [signet](https://en.bitcoin.it/wiki/Signet), you can try to get some coins to test [here](https://signetfaucet.com/)

## Architecture

To make this work, florestad is spawned as a foreground service, that keeps running on the background. When you need to access it, the wallet 
uses either the RPC or Electrum Protocol to request data. By doing so, we can have all the Bitcoin-related functionality inside a single 
application, without requiring additional steps from the user.

![Archtecture](/docs/Archtecture.png)

## Building

The easiest way to build this project is through [Android Studio](https://developer.android.com/studio). Inside it you can build and export the 
native APK to your devices. We currently support armv8 and x86_64. The minimum API level is 27.

## Screenshots

![screenshot1](/docs/screenshot1.webp)
![screenshot1](/docs/screenshot2.webp)

## Design
https://www.behance.net/gallery/211545769/Floresta-Wallet
