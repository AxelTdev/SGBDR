package bplusTree;


public class Entree
{
	private int valeur;
	private int Rid;
	
	public Entree(int numero)
	{
		this.valeur = numero;
	}

	/**
	 * @return the numero
	 */
	public int getValeur()
	{
		return valeur;
	}

	/**
	 * @return the rid
	 */
	public int getRid()
	{
		return Rid;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setValeur(int valeur)
	{
		this.valeur = valeur;
	}

	/**
	 * @param rid the rid to set
	 */
	public void setRid(int rid)
	{
		Rid = rid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "" + valeur;
	}
}
