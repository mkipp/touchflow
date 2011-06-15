package util;
import javax.swing.JOptionPane;

/**
 * Java-Klasse mit verschiedenen Erweiterungen und Abk�rzungen<br><br>
 * (c) 2008 Frederic Raber
 *
 */

public class RAClass {
	public RAClass() {};

	/**
	 * Abk�rzung f�r System.out.println
	 *
	 * @param text
	 *            Text zum ausgeben.
	 */
	
	public void Textout(String text)
	{
		System.out.println(text);
	}

	/**
	 * Abk�rzung f�r System.out.println
	 *
	 * @param obj
	 *            Objekt zum ausgeben.
	 */
	
	public void Textout(Object obj)
	{
		System.out.println(obj);
	}

	/**
	 * Abk�rzung f�r System.out.print - println ohne Zeilenumbruch
	 *
	 * @param text
	 *            Text zum ausgeben.
	 */
	public void Textoutn(String text)
	{
		System.out.print(text);
	}

	/**
	 * Abk�rzung f�r System.out.print - println ohne Zeilenumbruch
	 *
	 * @param obj
	 *           Objekt zum ausgeben.
	 */
	public void Textoutn(Object obj)
	{
		System.out.print(obj);
	}
	
	/**
	 * Erstellt ein Array und f�llt es mit Startwerten auf
	 *
	 * @param initobj
	 *            Startwert.
	 * @param size
	 *           L�nge des Array.
	 */
	public boolean[] Createarray (boolean initobj, int size)
	{
		boolean[] tmp=new boolean[size];
		for (int i=0; i<size; i++)
		{
			tmp[i]=initobj;
		}
		return tmp;
	}
	
	/**
	 * Erstellt ein Array und f�llt es mit Startwerten auf
	 *
	 * @param initobj
	 *            Startwert.
	 * @param size
	 *           L�nge des Array.
	 */
	
	public   int[] Createarray (int initobj, int size)
	{
		int[] tmp=new int[size];
		for (int i=0; i<size; i++)
		{
			tmp[i]=initobj;
		}
		return tmp;
	}
	
	/**
	 * Erstellt ein Array und f�llt es mit Startwerten auf
	 *
	 * @param initobj
	 *            Startwert.
	 * @param size
	 *           L�nge des Array.
	 */
	
	public  double[] Createarray (double initobj, int size)
	{
		double[] tmp=new double[size];
		for (int i=0; i<size; i++)
		{
			tmp[i]=initobj;
		}
		return tmp;
	}
	
	/**
	 * Erstellt ein Array und f�llt es mit Startwerten auf
	 *
	 * @param initobj
	 *            Startwert.
	 * @param size
	 *           L�nge des Array.
	 */
	
	public  String[] Createarray (String initobj, int size)
	{
		String[] tmp=new String[size];
		for (int i=0; i<size; i++)
		{
			tmp[i]=initobj;
		}
		return tmp;
	}
	
	
	/**
	 * Sucht das gr��te Element in einem Array
	 *
	 * @param Arr
	 *            zu durchsuchendes Array
	 */
	
	public  int Max (int[] Arr)
	{
		int tmp=Arr[0];
		for (int i=1; i< Arr.length;i++)
		{
			if (Arr[i]> tmp) {tmp=Arr[i];}
		}
		return tmp;
	}

	/**
	 * Sucht das kleinste Element in einem Array
	 *
	 * @param Arr
	 *            zu durchsuchendes Array
	 */
	
	public int Min (int[] Arr)
	{
		int tmp=Arr[0];
		for (int i=1; i< Arr.length;i++)
		{
			if (Arr[i]< tmp) {tmp=Arr[i];}
		}
		return tmp;
	}
	
	/**
	 * Berechnet den Durchschnitt der Werte eines Arrays
	 *
	 * @param Arr
	 *            zu durchsuchendes Array
	 */
	
	public  int Mid (int[] Arr)
	{
		int tmp=0;
		for (int i=0; i< Arr.length;i++)
		{
			tmp=tmp + Arr[i];
		}
		return Math.round(tmp/Arr.length);
	}
	
	/**
	 * F�llt ein Element mit Werten auf
	 *
	 * @param Arr
	 *            aufzuf�llendes Array
	 * @param Wert
	 *            F�llwert
	 */
	
	public  void Fillarray(int[] Arr, int Wert)
	{
		for (int i=0; i< Arr.length; i++)
		{
			Arr[i]=Wert;
		}
	}

	/**
	 * F�llt ein Element mit Werten auf
	 *
	 * @param Arr
	 *            aufzuf�llendes Array
	 * @param Wert
	 *            F�llwert
	 */
	
	public  void Fillarray(String[] Arr, String Wert)
	{
		for (int i=0; i< Arr.length; i++)
		{
			Arr[i]=Wert;
		}
	}
	

	/**
	 * F�llt ein Element mit Werten auf
	 *
	 * @param Arr
	 *            aufzuf�llendes Array
	 * @param Wert
	 *            F�llwert
	 */
	
	public  void Fillarray(boolean[] Arr, boolean Wert)
	{
		for (int i=0; i< Arr.length; i++)
		{
			Arr[i]=Wert;
		}
	}
	

	/**
	 * F�llt ein Element mit Werten auf
	 *
	 * @param Arr
	 *            aufzuf�llendes Array
	 * @param Wert
	 *            F�llwert
	 */
	
	public  void Fillarray(double[] Arr, double Wert)
	{
		for (int i=0; i< Arr.length; i++)
		{
			Arr[i]=Wert;
		}
	}

	/**
	 * Gibt eine MessageBox aus
	 *
	 * @param Text
	 *            Haupttext der Nachricht
	 * @param Title
	 *            Nachrichtentitel
	 * @param Symbol
	 *            Gibt an, welches Symbol angezeigt werden soll:
	 *            - "Error" f�r rotes Kreuz
	 *            - "Warning" f�r gelbes Ausrufezeichen
	 *            - "Question" f�r Fragezeichen
	 *            - was anderes f�r Info-Zeichen
	 */
	
	public static boolean ConfirmMsgbox (String Message)
	{
		return JOptionPane.showConfirmDialog(null, Message, "TouchFlow", JOptionPane.YES_NO_OPTION)==0;


	}
	
	/**
	 * Gibt eine MessageBox aus
	 *
	 * @param Text
	 *            Haupttext der Nachricht
	 * @param Title
	 *            Nachrichtentitel
	 * @param Symbol
	 *            Gibt an, welches Symbol angezeigt werden soll:
	 *            - "Error" f�r rotes Kreuz
	 *            - "Warning" f�r gelbes Ausrufezeichen
	 *            - "Question" f�r Fragezeichen
	 *            - was anderes f�r Info-Zeichen
	 */
	
	public static void msgbox (String Text, String Title, String Symbol)
	{
	 int sym;
	 sym=JOptionPane.INFORMATION_MESSAGE;
		if (Symbol=="Error") {sym=JOptionPane.ERROR_MESSAGE;}
		if (Symbol=="Warning") {sym=JOptionPane.WARNING_MESSAGE;}
		if (Symbol=="Question") {sym=JOptionPane.QUESTION_MESSAGE;}
			
		JOptionPane.showMessageDialog(null, 
			          Text, Title, 
			          sym);

	}
	
	/**
	 * Erstellt eine ganzzahlige Zufallszahl im angegebenen Intervall
	 *
	 * @param Untergrenze
	 *            Untergrenze des Intervalls
	 * @param Obergrenze
	 *            Untergrenze des Intervalls
	 */
	
	public  int RandomNr(int Untergrenze, int Obergrenze)
	{
		double tmp;
		
		tmp= (Obergrenze - Untergrenze + 1) * Math.random() + Untergrenze;
		return (int) tmp;

	}
	
	/**
	 * Pr�ft ein Array auf null-Eintr�ge
	 *
	 * @param Arr
	 *            zu pr�fendes Array
	 */
	
	public  boolean Hasnull (Object[] Arr)
	{
		for (Object obj:Arr)
		{
			if (obj==null){return true;}
		}
		return false;
	}
	
	/**
	 * Pr�ft ein Array auf null-Eintr�ge
	 *
	 * @param Arr
	 *            zu pr�fendes Array
	 */
	
	public boolean Hasnull (String[] Arr)
	{
		for (String obj:Arr)
		{
			if (obj==null){return true;}
		}
		return false;
	}
	
	/**
	 * Gibt illegalArgumentException aus
	 *
	 * @param Nachricht
	 *            auszugebende Nachricht
	 */
	
	public void Error (String Nachricht)
	{
		throw new IllegalArgumentException(Nachricht);
	}
	
	/**
	 * �ndert die Gr��e eines Array
	 *
	 * @param OldArr
	 *            zu �nderndes Array
	 * @param Newsize
	 *            neue Gr��e des Arrays
	 */
	
	public int[] Resizearray(int[] OldArr,int Newsize)
	{
		int[] tmp= new int[Newsize - OldArr.length];
		System.arraycopy(OldArr, 0, tmp, 0, OldArr.length);
		return tmp;
	}
	
	/**
	 * �ndert die Gr��e eines Array
	 *
	 * @param OldArr
	 *            zu �nderndes Array
	 * @param Newsize
	 *            neue Gr��e des Arrays
	 */
	
	public String[] Resizearray(String[] OldArr,int Newsize)
	{
		String[] tmp= new String[Newsize - OldArr.length];
		System.arraycopy(OldArr, 0, tmp, 0, OldArr.length);
		return tmp;
	}
	
	/**
	 * �ndert die Gr��e eines Array
	 *
	 * @param OldArr
	 *            zu �nderndes Array
	 * @param Newsize
	 *            neue Gr��e des Arrays
	 */
	
	public Double[] Resizearray(Double[] OldArr,int Newsize)
	{
		Double[] tmp= new Double[Newsize - OldArr.length];
		System.arraycopy(OldArr, 0, tmp, 0, OldArr.length);
		return tmp;
	}
	
	/**
	 * �ndert die Gr��e eines Array
	 *
	 * @param OldArr
	 *            zu �nderndes Array
	 * @param Newsize
	 *            neue Gr��e des Arrays
	 */
	
	public Boolean[] Resizearray(Boolean[] OldArr,int Newsize)
	{
		Boolean[] tmp= new Boolean[Newsize - OldArr.length];
		System.arraycopy(OldArr, 0, tmp, 0, OldArr.length);
		return tmp;
	}
	
	/**
	 * F�gt dem Array genau ein neues Element hinzu
	 *
	 * @param OldArr
	 *            zu �nderndes Array
	 * @param Item
	 *            hinzuzuf�gendes Objekt
	 */
	
	public Boolean[] AddItem (Boolean[] oldArr, boolean Item)
	{
		Boolean[]tmp=Resizearray(oldArr, oldArr.length + 1);
		tmp[oldArr.length + 1]=Item;
		return tmp;
	}
	
	/**
	 * F�gt dem Array genau ein neues Element hinzu
	 *
	 * @param OldArr
	 *            zu �nderndes Array
	 * @param Item
	 *            hinzuzuf�gendes Objekt
	 */
	
	public int[] AddItem (int[] oldArr, int Item)
	{
		int[]tmp=Resizearray(oldArr, oldArr.length + 1);
		tmp[oldArr.length + 1]=Item;
		return tmp;
	}
	
	/**
	 * F�gt dem Array genau ein neues Element hinzu
	 *
	 * @param OldArr
	 *            zu �nderndes Array
	 * @param Item
	 *            hinzuzuf�gendes Objekt
	 */
	
	public String[] AddItem (String[] oldArr, String Item)
	{
		String[]tmp=Resizearray(oldArr, oldArr.length + 1);
		tmp[oldArr.length + 1]=Item;
		return tmp;
	}
	
	/**
	 * F�gt dem Array genau ein neues Element hinzu
	 *
	 * @param OldArr
	 *            zu �nderndes Array
	 * @param Item
	 *            hinzuzuf�gendes Objekt
	 */
	
	public Double[] AddItem (Double[] oldArr, Double Item)
	{
		Double[]tmp=Resizearray(oldArr, oldArr.length + 1);
		tmp[oldArr.length + 1]=Item;
		return tmp;
	}
}
