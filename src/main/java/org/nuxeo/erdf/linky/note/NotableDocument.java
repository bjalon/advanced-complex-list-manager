/**
 * 
 */

package org.nuxeo.erdf.linky.note;

import static org.nuxeo.erdf.linky.note.NoteConstants.IS_SECURED_FIELD;
import static org.nuxeo.erdf.linky.note.NoteConstants.NOTE_SCHEMA;
import static org.nuxeo.erdf.linky.note.NoteConstants.SECURED_NOTE_XPATH;
import static org.nuxeo.erdf.linky.note.NoteConstants.UNSECURED_NOTE_XPATH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * @author bjalon
 */
public class NotableDocument {

	protected final DocumentModel doc;

	public NotableDocument(DocumentModel doc) {
		this.doc = doc;
	}

	public void save(CoreSession session) throws ClientException {
		session.saveDocument(doc);
	}

	public String getId() {
		return doc.getId();
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getNotes(boolean isSecured)
			throws ClientException {
		return (Map<String, String>) doc
				.getPropertyValue(getNoteFieldName(isSecured));
	}

	public void addNote(DocumentModel note) throws ClientException {
		if (!note.hasSchema(NOTE_SCHEMA)) {
			throw new ClientException("Given document is not a note: "
					+ note.getDocumentType());
		}

		Map<String, Object> newNote = new HashMap<String, Object>();
		Map<String, Object> values = note.getProperties(NOTE_SCHEMA);

		// Duplicate the note values in a list
		for (String key : values.keySet()) {
			if (!IS_SECURED_FIELD.equals(key)) {
				String fieldName = key.substring(key.lastIndexOf(":") + 1);
				newNote.put(fieldName, values.get(key));
			}
		}

		String targetListSchema = getNoteFieldName(note);

		// Fetch the list to add the note
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> notes = (List<Map<String, Object>>) doc
				.getPropertyValue(targetListSchema);

		if (notes == null) {
			notes = new ArrayList<Map<String, Object>>();
		}

		// Add the new note into the list
		notes.add(newNote);

		// replace this list into the field notes field
		doc.setPropertyValue(targetListSchema, (Serializable) notes);

	}

	private String getNoteFieldName(DocumentModel note) throws ClientException {
		Boolean isSecuredValue = getIsSecuredValue(note);
		String fieldName = getNoteFieldName(isSecuredValue);
		return (String) note.getPropertyValue(fieldName);
	}

	private boolean getIsSecuredValue(DocumentModel note)
			throws ClientException {
		return (Boolean) note.getPropertyValue(IS_SECURED_FIELD);

	}

	private String getNoteFieldName(boolean isSecured) {
		String targetListSchema = isSecured ? SECURED_NOTE_XPATH
				: UNSECURED_NOTE_XPATH;
		return targetListSchema;
	}

}
