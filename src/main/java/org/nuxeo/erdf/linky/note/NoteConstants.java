package org.nuxeo.erdf.linky.note;

public class NoteConstants {

	public static final String NOTE_DOC_TYPE = "ERDFNote";

	public static final String NOTES_SCHEMA = "erdf_notes";

	public static final String NOTE_SCHEMA = "erdf_note";

	public static final String IS_SECURED_FIELD = "isSecured";

	public static final String SECURED_NOTE_XPATH = NOTES_SCHEMA
			+ ":note_securisee";

	public static final String UNSECURED_NOTE_XPATH = NOTES_SCHEMA
			+ ":note_non_securisee";

	public static final String NOTE_XPATH = NOTE_SCHEMA + ":note";

	public static final String IS_SECURED_XPATH = NOTE_SCHEMA + ":" + IS_SECURED_FIELD;

	public static final String NOTE_CREATOR_XPATH = NOTE_SCHEMA + ":auteur";

}
