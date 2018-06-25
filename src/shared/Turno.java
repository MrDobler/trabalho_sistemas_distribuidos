package shared;

public class Turno {
	private long idUsuario;
	public boolean clienteAConfirmado = false;
	public boolean clienteBConfirmado = false;
	
	public Turno() {
		
	}

	public long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public boolean eMeuTurno(long idUsuario) {
		System.out.println("Cliente ID: " + idUsuario);
		System.out.println("Turno ID: " + this.idUsuario);
		System.out.println("Bool: " + (idUsuario == this.idUsuario));
		
		return this.idUsuario == idUsuario;
	}
}
