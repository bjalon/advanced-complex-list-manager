/**
 * 
 */

package org.nuxeo.erdf.linky.note;

import static org.nuxeo.erdf.linky.note.NoteConstants.IS_SECURED_XPATH;
import static org.nuxeo.erdf.linky.note.NoteConstants.NOTE_CREATOR_XPATH;
import static org.nuxeo.erdf.linky.note.NoteConstants.NOTE_DOC_TYPE;
import static org.nuxeo.erdf.linky.note.NoteConstants.NOTE_XPATH;

import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

/**
 * @author bjalon
 */
@Operation(id = AddNoteFromTask.ID, category = Constants.CAT_DOCUMENT, label = "Add Note From Task", description = "Cette op??ration ajoute une entr??e dans la liste des notes du document donn?? en input. Les champs calcul??s sont g??n??r??s automatiquement. Il faut que l'utilisateur lan??ant cette op??ration soit celui qui fait l'op??ration.")
public class AddNoteFromTask {

	public static final String ID = "AddNoteFromTask";

	public static final Log log = LogFactory.getLog(AddNoteFromTask.class);

	@Param(name = "note")
	protected String note;

	@Param(name = "isSecured")
	protected boolean isSecured;

	@Param(name = "creator", required = false)
	protected String creator;

	@Param(name = "save")
	protected boolean save;

	@Context
	protected OperationContext ctx;

	@OperationMethod(collector = DocumentModelCollector.class)
	public DocumentModel run(DocumentModel input) throws ClientException {
		if (input == null) {
			log.debug("Wait for a not null document as document to update");
			return input;
		}

		CoreSession session = ctx.getCoreSession();
		DocumentModel note = session.createDocumentModel(NOTE_DOC_TYPE);

		note.setPropertyValue(IS_SECURED_XPATH, isSecured);
		note.setPropertyValue(NOTE_XPATH, isSecured);
		if (creator != null && !creator.isEmpty()) {
			note.setPropertyValue(NOTE_CREATOR_XPATH, creator);
		}

		NotableDocument doc = input.getAdapter(NotableDocument.class);
		doc.addNote(note);

		if (save) {
			input = session.saveDocument(input);
			session.save();
		}

		return input;
	}

}
