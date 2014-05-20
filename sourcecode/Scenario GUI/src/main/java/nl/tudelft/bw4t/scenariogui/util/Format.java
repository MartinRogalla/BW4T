package nl.tudelft.bw4t.scenariogui.util;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * A class for the formatting of data.
 *
 * @author Nick
 *
 */
public final class Format {

    /** Prevents this class from being instantiated. */
    private Format() {
    }

    /** The document filter used. */
    private static DocumentFilter intDocumentFilter =
            new IntegerDocumentFilter();

    /**
     * The class for filtering a document so it only has integers.
     */
    private static class IntegerDocumentFilter extends DocumentFilter {

        @Override
        public void insertString(final FilterBypass fb, final int off,
                final String str,
                final AttributeSet attr) throws BadLocationException {
            // remove non-digits
            fb.insertString(off, str.replaceAll("\\D++", ""), attr);
        }

        /**
         * Deletes the region of text from <code>off</code> to
         * <code>off + len</code>, and replaces it with <code>str</code>.
         *
         * @param fb
         *            FilterBypass to circumvent calling back into the Document
         *            to change it.
         * @param off
         *            Offset, location in document.
         * @param len
         *            Length of text to delete.
         * @param str
         *            Text to insert, null indicates no text to insert.
         * @param attr
         *            AttributeSet indicating attributes of inserted text, null
         *            is legal.
         * @throws BadLocationException
         *             Reports bad locations within a document model.
         */
        @Override
        public void replace(final FilterBypass fb, final int off, final int len,
                final String str,
                final AttributeSet attr) throws BadLocationException {
            // remove non-digits
            fb.replace(off, len, str.replaceAll("\\D++", ""), attr);
        }
    }

    /**
     * Adds a filter to the text field that only allows numerical input.
     *
     * @param txt
     *            The text field.
     */
    public static void addIntegerDocumentFilterForTextField(
            final JTextField txt) {
        ((AbstractDocument) txt.getDocument())
                .setDocumentFilter(intDocumentFilter);
    }

}