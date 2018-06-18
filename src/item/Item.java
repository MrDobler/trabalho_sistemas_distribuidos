package item;

public class Item {
	
	private StringBuilder nome =  new StringBuilder();
	private int quant;
	
//	public Item(StringBuilder nome, int quant) {
//		this.nome = nome;
//		this.quant = quant;
//	}

	public StringBuilder getNome() {
		return nome;
	}

	public void setNome(StringBuilder nome) {
		this.nome = nome;
	}

	public int getQuant() {
		return quant;
	}

	public void setQuant(int quant) {
		this.quant = quant;
	}

}
