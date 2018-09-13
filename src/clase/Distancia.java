package clase;

public class Distancia {
	private int cantSaltos;
	private int posNodoAnt;
	public Distancia(int cantSaltos, int posNodoAnt) {
		super();
		this.cantSaltos = cantSaltos;
		this.posNodoAnt = posNodoAnt;
	}
	public int getCantSaltos() {
		return cantSaltos;
	}
	public int getPosNodoAnt() {
		return posNodoAnt;
	}
	public void setCantSaltos(int cantSaltos) {
		this.cantSaltos = cantSaltos;
	}
	public void setPosNodoAnt(int posNodoAnt) {
		this.posNodoAnt = posNodoAnt;
	}
	
}
