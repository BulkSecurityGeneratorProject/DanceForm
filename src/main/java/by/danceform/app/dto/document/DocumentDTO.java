package by.danceform.app.dto.document;

import javax.persistence.Lob;

/**
 * Created by dimonn12 on 22.10.2016.
 */
public class DocumentDTO extends UploadedDocumentDTO {

    @Lob
    private byte[] content;

    public DocumentDTO() {
    }

    public DocumentDTO(Long id) {
        super(id);
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
