/**
 * 
 */

package org.nuxeo.erdf.linky.note;

import static org.nuxeo.erdf.linky.note.NoteConstants.NOTE_DOC_TYPE;
import static org.nuxeo.erdf.linky.note.NoteConstants.NOTE_SCHEMA;

import java.io.Serializable;

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

	private static final long serialVersionUID = 1L;

	@In(create = true, required = false)
	protected transient CoreSession documentManager;

	@In(create = true)
	protected NavigationContext navigationContext;

	@In(create = true, required = false, value = NOTE_SCHEMA)
	protected DocumentModel erdfNote;

	@In(create = true, required = false)
	protected NuxeoPrincipal currentNuxeoPrincipal;

	@Factory(value = NOTE_SCHEMA)
	public DocumentModel createEmptyNote() throws ClientException {
		DocumentModel note = documentManager.createDocumentModel(NOTE_DOC_TYPE);
		return note;
	}

	public void save() throws ClientException {
		DocumentModel currentDocument = navigationContext.getCurrentDocument();
		NotableDocument doc = currentDocument.getAdapter(NotableDocument.class);

		doc.addNote(erdfNote);
	}

	public void cancel() {
		erdfNote = null;
	}

	public boolean canSeeSecuredNotes() {
		String group = Framework.getProperty(
				"org.nuxeo.erdf.linky.secured.notes.group.access",
				"administrators");
		return currentNuxeoPrincipal.isMemberOf(group);
	}

	public boolean canManageNotes() {
		String group = Framework.getProperty(
				"org.nuxeo.erdf.linky.secured.notes.group.manage",
				"administrators");
		return currentNuxeoPrincipal.isMemberOf(group);
	}

}
