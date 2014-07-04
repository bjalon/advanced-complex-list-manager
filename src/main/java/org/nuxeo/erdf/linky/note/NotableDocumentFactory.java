/**
 * 
 */

package org.nuxeo.erdf.linky.note;

import static org.nuxeo.erdf.linky.note.NoteConstants.NOTES_SCHEMA;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

/**
 * @author bjalon
 */
public class NotableDocumentFactory implements DocumentAdapterFactory {

	public static final Log log = LogFactory
			.getLog(NotableDocumentFactory.class);

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> itf) {
		if (doc.hasSchema(NOTES_SCHEMA)) {
			return new NotableDocument(doc);
		} else {
			log.info("Document can't be adapted as NotableDocument: "
					+ doc.getId() + "/" + doc.getPathAsString());
			return null;
		}
	}
}
