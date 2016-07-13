/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datis.irc.botProducer;

/**
 *
 * @author jeus
 */
public class MyBotMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        // Now start our bot up.
        MyBot bot = new MyBot();

        // Enable debugging output.
        bot.setVerbose(true);

        // Connect to the IRC server.
        bot.connect("irc.freenode.net");

        bot.listChannels();

        

        // Join the #pircbot channel.
        bot.joinChannel("#jeusjeus");
        bot.joinChannel("#technotux");
        bot.joinChannel("#python");
        bot.joinChannel("#archlinux");
        bot.joinChannel("#debian");
        bot.joinChannel("#freenode");
        bot.joinChannel("##security");
        bot.joinChannel("#haskell");
        bot.joinChannel("##linux");
        bot.joinChannel("#ubuntu");
        bot.joinChannel("#go-nuts");
        bot.joinChannel("#git");
        bot.joinChannel("##javascript");//-----------------------------------
        bot.joinChannel("#Node.js");
//        bot.joinChannel("#python-unregistered");
//        bot.joinChannel("#bitcoin");
//        bot.joinChannel("##networking");
//        bot.joinChannel("#bash");
//        bot.joinChannel("#vim");
//        bot.joinChannel("#gentoo");
//        bot.joinChannel("#znc");
//        bot.joinChannel("#postgresql");
//        bot.joinChannel("#docker");
//        bot.joinChannel("##c");
//        bot.joinChannel("##c++");
//        bot.joinChannel("#puppet");
//        bot.joinChannel("#ruby");
//        bot.joinChannel("#openstack");
//        bot.joinChannel("#emacs");
//        bot.joinChannel("#nginx");
//        bot.joinChannel("##math");
//        bot.joinChannel("##electronics");
//        bot.joinChannel("#weechat");

    }

}
