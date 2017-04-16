
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

public class Quickstart extends Initials {

	private static final String UPLOAD_FILE_PATH = "D:\\googledrive client\\files\\wild.jpg";
	private static final java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH);
	private static final String DIR_FOR_DOWNLOADS = "D:\\downloads\\";
	private static Drive service;
	private static File uploadedFile;
	private static String fileId;

	// List out files in the drive.
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
	private static File uploadFile() throws IOException {
		
		File fileMetadata = new File();
		fileMetadata.setName(UPLOAD_FILE.getName());
		FileContent mediaContent = new FileContent("image/jpeg", UPLOAD_FILE);
		Drive.Files.Create insert = service.files().create(fileMetadata, mediaContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(false);							//set true for direct upload and false for resume upload
		uploader.setProgressListener(new UploadProgressListener());
		return insert.execute();
	}
	
	// Downloads the uploaded file
	  private static void downloadFile(File uploadedFile)
	      throws IOException {
	    // create parent directory (if necessary)
	    java.io.File parentDir = new java.io.File(DIR_FOR_DOWNLOADS);
	    if (!parentDir.exists() && !parentDir.mkdirs()) {
	      throw new IOException("Unable to create parent directory");
	    }
	    OutputStream out = new FileOutputStream(new java.io.File(parentDir, uploadedFile.getName()));
	    MediaHttpDownloader downloader =
	        new MediaHttpDownloader(HTTP_TRANSPORT, service.getRequestFactory().getInitializer());
	    downloader.setDirectDownloadEnabled(false);
	    downloader.setProgressListener(new DownloadProgressListener());
	    service.files().get(fileId)
        .executeMediaAndDownloadTo(out);
	    System.out.println("Download success..");		//since the progress listener isn't working at the moment
	  }

	public static void main(String[] args) throws IOException {

		// Build a new authorized API client service.
		service = getDriveService();
		boolean cont = true;
		int choice;
		Scanner sc = new Scanner(System.in);

		while (cont) {
			System.out.println("\nEnter Your Choice for the following operations: \n");
			System.out.println("1. Upload a file");
			System.out.println("2. Download the uploaded file");
			System.out.println("3. List out all the files in drive");
			System.out.println("4. Exit");

			choice = sc.nextInt();
			switch (choice) {
			case 1:
				uploadedFile = uploadFile();
				fileId = uploadedFile.getId();
				break;

			case 2:
				downloadFile(uploadedFile);
				break;
				
			case 3:
				retrieve();
				break;

			case 4:
				cont = false;
				break;

			default:
				System.out.println("Invalid Option");
				break;
			}

		}

	}

}