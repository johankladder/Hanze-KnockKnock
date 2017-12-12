package nl.hanze.distapps;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

public class KnockKnockProtocol {
    private static final int WAITING = 0;
    private static final int SETLANG = 1;
    private static final int SET_TYPE = 2;
    private static final int TYPE = 3;
    private static final int WORD = 4;
    private static final int SENTENCE = 5;


    private int state = WAITING;

    private static final int[] STATE_ARRAY = new int[]{
            WAITING, SETLANG, SET_TYPE, TYPE, WORD, SENTENCE
    };

    private Translater translater;

    public KnockKnockProtocol() {
        this.translater = new Translater();
    }

    public String processInput(String theInput) {
        String theOutput = null;

        if (theInput != null && theInput.equals("back")) {
            back(state);
        }

        switch (state) {
            case WAITING:
                theOutput = "Welke taal?";
                state = SETLANG;
                break;
            case SETLANG:
                if (this.translater.languageAvailable(theInput)) {
                    this.translater.setLanguage(theInput);
                    state = TYPE;
                    theOutput = "Taal: " + theInput + " ingesteld";
                } else {
                    theOutput = "Taal niet herkend, probeer opnieuw. Welke taal?";
                    state = SETLANG;
                }
                break;
            case SET_TYPE:
                theOutput = "Wilt u een woord of zin vertalen?";
                state = TYPE;
                break;
            case TYPE:
                switch (theInput) {
                    case "word":
                        theOutput = "Vul een woord in om te vertalen";
                        state = WORD;
                        break;
                    case "sentence":
                        theOutput = "Vul een zin in om te vertalen";
                        state = SENTENCE;
                        break;
                    default:
                        theOutput = "Input niet herkend. Kies voor 'word' of 'sentence'";
                        break;
                }
                break;
            case WORD:
                String word = this.translater.getTranslatedWord(theInput);
                if (word != null) {
                    theOutput = "Translated word: " + word;
                } else {
                    theOutput = "Word not found";
                }
                break;
            case SENTENCE:
                String[] words = theInput.split(" ");
                theOutput = "";
                for (String w : words) {
                    theOutput += this.translater.getTranslatedWord(w) + " ";
                }
                break;
        }

        return theOutput;
    }

    public void back(int currentState) {
        if (currentState > 1) {
            state = STATE_ARRAY[currentState - 2];
        }
    }
}
