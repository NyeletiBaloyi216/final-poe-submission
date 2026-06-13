/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.login;



import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;



    /**
 *
 * @author baloy
 */
public class QuickchatTest {
    
    @Before
    public void setUp() {
        Quickchat.resetArrays();
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        Quickchat.populateTestData();
        String[] sent = Quickchat.getSentMessagesArray();
        assertEquals("Did you get the cake?", sent[0]);
        assertEquals("It is dinner time", sent[1]);
        assertEquals(2, sent.length);
    }

    @Test
    public void testMessagesArrayContainsExpectedTestData() {
        Quickchat.populateTestData();
        String[] stored = Quickchat.getStoredMessagesArray();
        String[] recipients = Quickchat.getStoredRecipientsArray();
        assertEquals("Where are you? You are late! I have asked you to be on time.", stored[0]);
        assertEquals("+27838884567", recipients[0]);
        assertEquals("Ok, I am leaving without you.", stored[1]);
        assertEquals("+27838884567", recipients[1]);
    }

    @Test
    public void testDisplayLongestMessage() {
        Quickchat.populateTestData();
        String longest = Quickchat.displayLongestStoredMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    @Test
    public void testSearchMessageID() {
        Quickchat.populateTestData();
        String result = Quickchat.searchMessageID("MSG002");
        assertEquals("Where are you? You are late! I have asked you to be on time.", result);
    }

    @Test
    public void testSearchRecipientMessages() {
        Quickchat.populateTestData();
        String[] result = Quickchat.searchRecipientMessages("+27838884567");
        assertEquals(2, result.length);
        assertEquals("Where are you? You are late! I have asked you to be on time.", result[0]);
        assertEquals("Ok, I am leaving without you.", result[1]);
    }

    @Test
    public void testDeleteMessageByHash() {
        Quickchat.populateTestData();
        String[] hashes = Quickchat.getMessageHashArray();
        assertTrue(hashes.length > 0);
        boolean deleted = Quickchat.deleteMessageByHash(hashes[0]);
        assertTrue(deleted);
        String[] stored = Quickchat.getStoredMessagesArray();
        for (String msg : stored) {
            assertNotEquals("Where are you? You are late! I have asked you to be on time.", msg);
        }
        assertEquals(1, Quickchat.getStoredCount());
    }

    @Test
    public void testDisplayReportArraysPopulated() {
        Quickchat.populateTestData();
        assertNotNull(Quickchat.getSentMessagesArray());
        assertNotNull(Quickchat.getDisregardedMessagesArray());
        assertNotNull(Quickchat.getStoredMessagesArray());
        assertNotNull(Quickchat.getMessageHashArray());
        assertNotNull(Quickchat.getMessageIDArray());
        assertEquals(2, Quickchat.getSentCount());
        assertEquals(1, Quickchat.getDisregardedCount());
        assertEquals(2, Quickchat.getStoredCount());
    }
}