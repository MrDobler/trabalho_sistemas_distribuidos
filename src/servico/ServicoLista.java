package servico;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import shared.Item;
import shared.Status;
import shared.Turno;

public class ServicoLista extends UnicastRemoteObject implements ServicoListaInterface {

	private static final long serialVersionUID = 1L;
	
	private Status status;
	
	private List<Item> listaCasamento = new ArrayList<Item>();
	private Turno turno = new Turno();
	
	private static long clienteA = 0L;
	private static long clienteB = 0L;
	private long idConfirmou;
	

	public ServicoLista() throws RemoteException {
		super();
		this.status = Status.ESPERANDO_CONJUGES;
	}
	

	
	@Override
	public void confirmar(long id) throws RemoteException {
		System.out.println("---------- CONFIRMAR ----------");
		System.out.println("Clienta A: " + clienteA);
		System.out.println("Cliente B: " + clienteB);
		System.out.println("ID: " + id);
		
		if(id == clienteA) {
			System.out.println("Igual A");
			this.turno.clienteAConfirmado = true;
		} else if(id == clienteB) {
			System.out.println("Igual b");
			this.turno.clienteBConfirmado = true;
		}
		
		System.out.println("-------- CONFIRMA FIM -===------");
	}
	
	@Override
	public long idConfirmou() throws RemoteException {
		return this.idConfirmou;
	}

	@Override
	public void menu(long id) throws RemoteException {

	}
	
//	@Override
//	public void teste() throws RemoteException {
//		System.out.println("ID Cliente A: "+clienteA+"\t"+"ID Cliente B: "+clienteB);
//		System.out.println("\n"+vez);
//		int i = 1;
//		for (Item it: listaCasamento) {
//			model.addRow(new Object[] {i, it.getNome(), it.getQuant()});
//			System.out.println("ID"+i+"\t Nome: "+it.getNome());
//			i++;
//		}
//	}

	@Override
	public void addItem(String nome, int quant) throws RemoteException {
		Item item = new Item();
		item.setNome(new StringBuilder(nome));
		item.setQuant(quant);
		listaCasamento.add(item);
	}
	
	@Override
	public Iterable<Item> getLista() throws RemoteException {
		return this.listaCasamento;
	}

	@Override
	public void removeItem(String nomeItemARemover) {
		StringBuilder itemDaLista = new StringBuilder();
		for (Iterator<Item> iter = listaCasamento.listIterator(); iter.hasNext();) {
			itemDaLista = iter.next().getNome();
			if (itemDaLista.equals(new StringBuilder(nomeItemARemover))) {
				iter.remove();
			}
		}
	}
	
	@Override
	public void setIdCliente(long id) throws RemoteException {
		System.out.println("Cliente A: " + clienteA);
		System.out.println("Cliente B: " + clienteB);
		System.out.println("Cliente ID: " + id);
		System.out.println("Status: " + this.status);
		
		if (clienteA == 0L) {
			clienteA = id;
			this.turno.setIdUsuario(id);
			this.status = Status.ESPERANDO_CONJUGES;
		} else if(clienteB == 0L) {
			clienteB = id;
			this.status = Status.RODANDO;
		}
		
		System.out.println("Status: " + this.status);
	}
	
	@Override
	public void mudarTurno(long clienteID) throws RemoteException {
		if(clienteID == clienteA)
			this.turno.setIdUsuario(clienteB);
		else if(clienteID == clienteB)
			this.turno.setIdUsuario(clienteA);
	}
	
	@Override
	public boolean checkMeuTurno(long clienteID) throws RemoteException {
		return this.turno.eMeuTurno(clienteID);
	}
	
	@Override
	public long getIDTurno() throws RemoteException{
		return this.turno.getIdUsuario();
	}
	
	public Status getStatus() throws RemoteException {
		return this.status;
	}
}
