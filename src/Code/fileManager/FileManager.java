package Code.fileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



import Code.Rid.Rid;
import Code.dbdef.DBDef;
import Code.heapFile.HeapFile;
import Code.pages.PageId;
import Code.record.Record;
import Code.reldef.RelDef;
import Code.type.Type;

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

	public List<Record> join(String relation1, String relation2, int col1, int col2) {

		// Etape 1 création de la nouvelle relation
		// récupération des données des deux relations

		HeapFile r1 = null;
		HeapFile r2 = null;
		RelDef relation = null;
		for (int i = 0; i < this.getHeapFile().size(); i++) {
			if (this.getHeapFile().get(i).getReldef().getNomRelation().equals(relation2)) {
				r2 = this.getHeapFile().get(i);
			}

			if (this.getHeapFile().get(i).getReldef().getNomRelation().equals(relation1)) {
				r1 = this.getHeapFile().get(i);

			}
		}
		// vérifie le remplissage des heapfiles
		if (r1 != null && r2 != null) {

			// construction de la nouvelle relation

			int recordSize = r1.getReldef().getRecordSize() + r2.getReldef().getRecordSize();
			int slotCount = r1.getReldef().getSlotCount() + r2.getReldef().getSlotCount();
			relation = new RelDef(DBDef.nbRelation, recordSize, slotCount);
			relation.setNomRelation(r1.getReldef().getNomRelation() + " >< " + col1 + " = " + col2 + "  "
					+ r2.getReldef().getNomRelation());
			int tailleTab = r1.getReldef().getTypeColonne().length + r2.getReldef().getTypeColonne().length;
			Type[] tabValue = new Type[tailleTab];

			for (int i = 0; i < r1.getReldef().getTypeColonne().length; i++) {
				tabValue[i] = r1.getReldef().getTypeColonne()[i];
			}

			for (int y = r1.getReldef().getTypeColonne().length, i = 0; y < tailleTab; y++, i++) {
				tabValue[y] = r2.getReldef().getTypeColonne()[i];
			}

			relation.setTypeColonne(tabValue);

			

		}

		// Etape 2 Equi - Jointure

		boolean pageFinieR1 = false;
		boolean pageFinieR2 = false;
		
		int indique = 0;
		PageId pgR1 = null;
		PageId pgR2 = null;

		Record[] recordR1;
		Record[] recordR2;

		// resultat list
		List<Record> recordJoin = new ArrayList<>();
		System.out.println("nb colonne "+col1 + "   "+ col2);
		for (int i = 2; i < r1.getReldef().getSlotCount(); i++) {// les datasPages sont accessibles a partir de 2
			pgR1 = new PageId(r1.getReldef().getFileIdx(), i);
			//charge dataPage de relation 1
			recordR1 = r1.getRecordsInDataPage(pgR1);
			
			for (int y = 2;y <r2.getReldef().getSlotCount(); y++) {
				pgR2 = new PageId(r2.getReldef().getFileIdx(), y);
				//charge dataPage de relation 2
				recordR2 = r2.getRecordsInDataPage(pgR2);
				
				// comparaisons
				indique++;
				for (int x = 0; x < recordR1.length; x++) {

					for (int o = 0; o < recordR2.length; o++) {
						if (recordR1[x].getValues()[col1].equals(recordR2[o].getValues()[col2])) {
							
							Record rd = new Record(relation);
							//construction du nouveau record
							String []tabValue = new String[recordR1[x].getValues().length+recordR2[o].getValues().length];
							//copie des valeurs des records dans un seul tableau
					        System.arraycopy(recordR1[x].getValues(), 0, tabValue, 0, recordR1[x].getValues().length);
					        System.arraycopy(recordR2[o].getValues(), 0, tabValue, recordR1[x].getValues().length, recordR2[o].getValues().length);
							rd.setValues(tabValue);
							recordJoin.add(rd);
							
						}
					}
				}

				
			}
		}
		System.out.println("ddddddddddddddddddddddd" + indique);
		return recordJoin;

		

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
