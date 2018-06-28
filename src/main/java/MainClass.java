import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainClass {

	private static final String FILE_PATH = "C:\\Users\\ahmed.ehab\\Desktop\\SR.xlsx";
	private static final String SCP = "scp ";
	private static final String Dest = "siebel:10.198.52.200";

	private static XSSFWorkbook workBook;
	private static XSSFSheet sheet;
	private static XSSFRow row;
	
//	private static Workbook workBook;
//	private static Sheet sheet;
//	private static Row row;

	private static int sheetIndex = 0;

	private static FileInputStream inStream;

	private static String colValue;
	private static BufferedWriter writer;

	private static StringBuilder sb;
	private static int batchSize = 25000;

	public static void main(String[] args) throws IOException {
		
		BasicConfigurator.configure();

		sb = new StringBuilder();

		File file = new File(FILE_PATH);

		inStream = new FileInputStream(file);

		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter max row count: ");

		int fileSize = Integer.valueOf(scanner.nextLine());

		readFromExcell(inStream, fileSize);
		scanner.close();

	}

	private static void readFromExcell(FileInputStream xinStream, int fileSize) throws IOException {

		int x = 0;
		
		workBook = new XSSFWorkbook(xinStream);
		sheet = workBook.getSheetAt(sheetIndex);
		
//		 workBook =  StreamingReader.builder()
//		        .rowCacheSize(1000)    // number of rows to keep in memory (defaults to 10)
//		        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
//		        .open(xinStream);     // InputStream or File for XLSX file (required)
		
		 sheet = workBook.getSheetAt(sheetIndex);
		
		int numberOfBatches = fileSize / batchSize;
		for (int i = 1; i <= numberOfBatches; i++) {

			genrateAttCom(x, x + batchSize);
			writeToTextFile(SCP, sb, Dest, i);
			xinStream.close();
			x = x + batchSize;

//			System.out.println("Choppingr " + i + " is Running");

		}

	}

	private static void genrateAttCom(int startRow, int maxRow) {
		Cell cell;

		sb.append(SCP);

		// get cell value
		for (int i = startRow; i < maxRow; i++) {
			row = sheet.getRow(i);

//			System.out.println(String.valueOf(row.getRowNum()));
			// Iterator<Cell> cellIterator = row.cellIterator();
			if (row.getRowNum() == maxRow) {

//				System.out.println("Row Number" + i + " is Running");

				break;
			} else {

				// while (cellIterator.hasNext()) {

				cell = row.getCell(0);
				// string builder will be used here
				colValue = cell.getStringCellValue() + " ";
				sb.append(colValue);

				// }
			}
		}

	}

	private static void writeToTextFile(String scp2, StringBuilder stringBuilder, String dest2, int batchNumber)
			throws IOException {

		stringBuilder.append(dest2);

		File batchFile = new File("C:\\Users\\ahmed.ehab\\Desktop\\" + "b" + String.valueOf(batchNumber) + ".sh");

		writer = new BufferedWriter(new FileWriter(batchFile));
		writer.write(String.valueOf(stringBuilder));
		writer.close();
		System.out.println("Batch Number" + batchNumber + " Created");
		sb.setLength(0);

	}

}
