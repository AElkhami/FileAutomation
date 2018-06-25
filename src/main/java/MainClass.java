import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainClass {

	private static final String FILE_PATH = "C:\\Users\\ElkhamiTech\\Desktop\\BookTest.xlsx";
	private static final String SCP = "scp ";
	private static final String Dest = "siebel:10.198.52.200";

	private static XSSFWorkbook workBook;
	private static XSSFSheet sheet;
	private static XSSFRow row;

	private static int sheetIndex = 0;
	private static int batchNumber = 1;

	private static FileInputStream inStream;

	private static String colValue;
	private static BufferedWriter writer;
	
	private static StringBuilder sb;

	public static void main(String[] args) {

		try {
			sb =new StringBuilder(); 
			inStream = new FileInputStream(new File(FILE_PATH));
			readFromExcell(inStream);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void readFromExcell(FileInputStream inStream) {

		try {
			workBook = new XSSFWorkbook(inStream);
			sheet = workBook.getSheetAt(sheetIndex);

			Iterator<Row> rowIterator = sheet.iterator();

			int rowCount = sheet.getPhysicalNumberOfRows();

			while (rowIterator.hasNext()) {

				row = (XSSFRow) rowIterator.next();

				row.setRowNum(8);
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					// string builder will be used here
					colValue = cell.getStringCellValue() + " ";
					 
					sb.append(colValue);

				}

			}

			writeToTextFile(SCP, sb, Dest);
			inStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void writeToTextFile(String scp2, StringBuilder stringBuilder, String dest2) throws IOException {

		String batch = scp2 + stringBuilder + dest2;

		File batchFile = new File("C:\\Users\\ElkhamiTech\\Desktop\\" + "b" + String.valueOf(batchNumber) + ".sh");

		writer = new BufferedWriter(new FileWriter(batchFile));
		writer.write(batch);
		writer.close();

	}

}
