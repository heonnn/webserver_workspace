package board.model.dto;

import java.util.List;

public class BoardExt extends Board {
	
	private int attachCount;
	private List<Attachment> attachments;

	public int getAttachCount() {
		return attachCount;
	}

	public void setAttachCount(int attachCount) {
		this.attachCount = attachCount;
	}
	
	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "BoardExt [attachCount=" + attachCount + ", attachments=" + attachments + ", toString()="
				+ super.toString() + "]";
	}
	
	
}
