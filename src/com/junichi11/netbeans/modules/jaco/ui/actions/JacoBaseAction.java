/*
 * The MIT License
 *
 * Copyright 2015 junichi11.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.junichi11.netbeans.modules.jaco.ui.actions;

import com.junichi11.netbeans.modules.jaco.Jaco;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.annotations.common.CheckForNull;
import org.netbeans.editor.BaseAction;
import org.openide.text.NbDocument;

/**
 *
 * @author junichi11
 */
public abstract class JacoBaseAction extends BaseAction {

    private static final long serialVersionUID = 738619999212761729L;
    private static final Logger LOGGER = Logger.getLogger(JacoBaseAction.class.getName());

    private final String name;

    public JacoBaseAction(String methodName) {
        this.name = methodName;
    }

    @Override
    public void actionPerformed(ActionEvent ae, final JTextComponent textComponent) {
        String selectedText = textComponent.getSelectedText();
        final StyledDocument document = getStyledDocument(textComponent);
        if (document == null) {
            return;
        }

        int selectionStart;
        int length;
        if (selectedText != null && !selectedText.isEmpty()) {
            selectionStart = textComponent.getSelectionStart();
        } else {
            selectionStart = 0;
            selectedText = textComponent.getText();
        }
        length = selectedText.length();

        // convert
        final String convertedText = Jaco.getInstance().runStaticMethod(name, selectedText);
        if (convertedText == null || convertedText.equals(selectedText)) {
            return;
        }

        // replace
        final int startPosition = selectionStart;
        final int removeLength = length;
        final int tmpCaretPosition = textComponent.getCaretPosition();
        try {
            NbDocument.runAtomicAsUser(document, new Runnable() {
                @Override
                public void run() {
                    try {
                        document.remove(startPosition, removeLength);
                        document.insertString(startPosition, convertedText, null);
                        textComponent.setCaretPosition(tmpCaretPosition);
                    } catch (BadLocationException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (BadLocationException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    @CheckForNull
    private StyledDocument getStyledDocument(JTextComponent textComponent) {
        Document document = textComponent.getDocument();
        if (document == null) {
            return null;
        }
        if (!(document instanceof StyledDocument)) {
            return null;
        }
        return (StyledDocument) document;

    }

}
