package Code.fileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Code.Rid.Rid;
import Code.dbdef.DBDef;
import Code.heapFile.HeapFile;
import Code.record.Record;
import Code.reldef.RelDef;

/*
 * 
 * Classe singleton avec une Liste de HeapFile
 */
public class FileManager {
	public static final FileManager INSTANCE = new FileManager();

	private static List<HeapFile> heapFile = new ArrayList<>();

	private FileManager() {

	}

	/*
	 * Fait appel à la variable static qui instancie l'objet
	 */
	public static FileManager getInstance() {
		return INSTANCE;
	}

	public List<HeapFile> getHeapFile() {
		return heapFile;
	}

	public static void Init() {

		List<RelDef> listReldef = DBDef.reldef;
		for (int i = 0; i < listReldef.size(); i++) {
			HeapFile heap = new HeapFile(listReldef.get(i));
			heapFile.add(heap);
		}
	}

	public static void CreateRelationFile(RelDef reldef) {
		HeapFile heap = new HeapFile(reldef);
		heapFile.add(heap);
		heap.createNewOnDisk();
	}

	public Rid InsertRecordInRelation(Record rd, String relName) throws IOException {
		Rid rid = null;
		for (int i = 0; i < heapFile.size(); i++) {

			if (relName.equals(heapFile.get(i).getReldef().getNomRelation())) {
				rd.setReldef(heapFile.get(i).getReldef());
				rid = heapFile.get(i).InsertRecord(rd);
				break;
			}

		}
		return rid;
	}

	public List<Record> SelectAllFromRelation(String relName) {
		List<Record> listRecord = new ArrayList<>();
		Record[] tabRecord = null;
		for (int i = 0; i < heapFile.size(); i++) {

			if (relName.equals(heapFile.get(i).getReldef().getNomRelation())) {
				tabRecord = heapFile.get(i).GetAllRecords();
				break;
			}

		}
		for (int i = 0; i < tabRecord.length; i++) {
			listRecord.add(tabRecord[i]);
		}
		return listRecord;
	}

	public void reset() {
		heapFile.clear();
	}

	public List<Record> SelectFromRelation(String relName, int idxCol, String valeur) {
		List<Record> listRecord = SelectAllFromRelation(relName);
		List<Record> recordList = new ArrayList<>();
		for (int i = 0; i < listRecord.size(); i++) {
			if (listRecord.get(i).getValues()[idxCol - 1].equals(valeur)) {// - 1 car sinon la colonne 1 correspond a la

				recordList.add(listRecord.get(i));
			}
		}

		return recordList;
	}

}
