/**
 * 
 */

package org.nuxeo.erdf.linky.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.runtime.api.Framework;

/**
 * 
 * Manager for Notes with security
 */
@Name("noteManager")
@Scope(ScopeType.EVENT)
public class NoteManagerBean implements Serializable {

	private static final String IS_SECURED_FIELD = "isSecured";

	private static final String NOTE_SCHEMA = "erdf_note";

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(NoteManagerBean.class);

	public static final String SECURED_NOTE_SCHEMA = "erdf_notes:note_securisee";

	public static final String UNSECURED_NOTE_SCHEMA = "erdf_notes:note_non_securisee";

	@In(create = true, required = false)
	protected transient CoreSession documentManager;

	@In(create = true)
	protected NavigationContext navigationContext;

	@In(create = true, required = false, value=NOTE_SCHEMA)
	protected DocumentModel erdfNote;

	@In(create = true, required = false)
    protected NuxeoPrincipal currentNuxeoPrincipal;


	@Factory(value = NOTE_SCHEMA)
	public DocumentModel createEmptyNote() throws ClientException {
		DocumentModel note = documentManager.createDocumentModel("ERDFNote");
		return note;
	}

	public void save() throws ClientException {
		DocumentModel currentDocument = navigationContext.getCurrentDocument();

		Map<String, Object> newNote = new HashMap<String, Object>();
		Map<String, Object> values = erdfNote.getProperties(NOTE_SCHEMA);

		// Duplicate the note values in a list
		for (String key : values.keySet()) {
			if (!"erdf_note:isSecured".equals(key)) {
				String fieldName = key.substring(key.lastIndexOf(":") + 1);
				newNote.put(fieldName, values.get(key));
			}
		}
		
		// Which field the note will be added in ? 
		String targetListSchema = erdfNote
				.getPropertyValue(NOTE_SCHEMA + ":" + IS_SECURED_FIELD) != null && (Boolean) erdfNote
				.getPropertyValue(NOTE_SCHEMA + ":" + IS_SECURED_FIELD) ? SECURED_NOTE_SCHEMA
				: UNSECURED_NOTE_SCHEMA;

		// Fetch the list to add the note
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> notes = (List<Map<String, Object>>) currentDocument
				.getPropertyValue(targetListSchema);
		
		if (notes == null) {
			notes = new ArrayList<Map<String, Object>>();
		}

		// Add the new note into the list
		notes.add(newNote);
		
		// replace this list into the field notes field
		currentDocument.setPropertyValue(targetListSchema, (Serializable) notes);

	}

	public void cancel() {
		erdfNote = null;
	}
	
	public boolean canSeeSecuredNotes() {
		String group = Framework.getProperty("org.nuxeo.erdf.linky.secured.notes.group.access", "administrators");
		return currentNuxeoPrincipal.isMemberOf(group);
	}
	
	public boolean canManageNotes() {
		String group = Framework.getProperty("org.nuxeo.erdf.linky.secured.notes.group.manage", "administrators");
		return currentNuxeoPrincipal.isMemberOf(group);
	}
	
	

}
