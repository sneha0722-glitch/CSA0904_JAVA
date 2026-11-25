import java.io.IOException;

class ExamException extends Exception {
    ExamException(String message) { super(message); }
    ExamException(String message, Throwable cause) { super(message, cause); }
}
class ExamClosedException extends ExamException {
    ExamClosedException(String message) { super(message); }
}
class NotRegisteredException extends ExamException {
    NotRegisteredException(String message) { super(message); }
}
class SubmissionStorageException extends ExamException {
    SubmissionStorageException(String message, Throwable cause) { super(message, cause); }
}
class ExamStorage {
    void save(String fileName, long sizeKb) throws SubmissionStorageException {
        try {
            if (sizeKb > 500) throw new IOException("Disk quota exceeded");
            System.out.println("Saved " + fileName);
        } catch (IOException e) {
            throw new SubmissionStorageException("Could not store " + fileName, e);
        }
    }
}
class ExamService {
    private final ExamStorage storage = new ExamStorage();
    void submit(boolean examOpen, boolean registered, String file, long size)
            throws ExamException {
        if (!examOpen) throw new ExamClosedException("Exam is closed.");
        if (!registered) throw new NotRegisteredException("Student is not registered.");
        storage.save(file, size);
    }
}
public class ExamPortalDemo {
    public static void main(String[] args) {
        ExamService service = new ExamService();
        Object[][] tests = {
            {true, true, "answer01.pdf", 120L},
            {false, true, "answer02.pdf", 100L},
            {true, false, "answer03.pdf", 100L},
            {true, true, "answer04.pdf", 800L}
        };
        for (Object[] t : tests) {
            try {
                service.submit((boolean)t[0], (boolean)t[1], (String)t[2], (long)t[3]);
            } catch (ExamException e) {
                System.out.println("Error: " + e.getMessage());
                if (e.getCause() != null) System.out.println("Cause: " + e.getCause().getMessage());
                e.printStackTrace(System.out);
            }
        }
    }
}
/*
Checked exceptions force callers to handle/declare expected business failures.
Unchecked exceptions suit programming errors/invalid state. 'throw' raises
an exception; 'throws' declares possible propagation. Wrapping the IOException
as the cause preserves the low-level reason while exposing a meaningful
storage-layer message. Flow: controller -> service -> storage -> IOException
-> SubmissionStorageException -> service/controller handler.
*/