
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Quickstart extends Initials {

	private static final String UPLOAD_FILE_PATH = "D:\\googledrive client\\files\\wild.jpg";
	private static final java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH);
	private static Drive service;

	// Print the names and IDs for up to 10 files.
	private static void retrieve() throws IOException {

		FileList result = service.files().list().setPageSize(20).setFields("nextPageToken, files(id, name)").execute();
		List<File> files = result.getFiles();
		if (files == null || files.size() == 0) {
			System.out.println("No files found.");
		} else {
			System.out.println("Files in drive:");
			for (File file : files) {
				System.out.printf("%s\n", file.getName());
			}
		}
	}
	
	// perform a simple upload here
	private static void store() throws IOException {
		
		File fileMetadata = new File();
		fileMetadata.setName(UPLOAD_FILE.getName());
		FileContent mediaContent = new FileContent("image/jpeg", UPLOAD_FILE);
		Drive.Files.Create insert = service.files().create(fileMetadata, mediaContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(true);							//set true for direct upload and false for resume upload
		uploader.setProgressListener(new UploadProgressListener());
		insert.execute();
	}

	public static void main(String[] args) throws IOException {

		// Build a new authorized API client service.
		service = getDriveService();
		boolean cont = true;
		int choice;
		Scanner sc = new Scanner(System.in);

		while (cont) {
			System.out.println("\nEnter Your Choice: \n");
			System.out.println("1. Store");
			System.out.println("2. Retrieve");
			System.out.println("3. Exit");

			choice = sc.nextInt();
			switch (choice) {
			case 1:
				Quickstart.store();
				break;

			case 2:
				Quickstart.retrieve();
				break;

			case 3:
				cont = false;
				break;

			default:
				System.out.println("Invalid Option");
				break;
			}

		}

	}

}