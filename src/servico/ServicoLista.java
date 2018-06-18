package servico;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import item.Item;

public class ServicoLista extends UnicastRemoteObject implements ServicoListaInterface {

	private static final long serialVersionUID = 1L;
	
	private static List<Item> listaCasamento = new ArrayList<Item>();

	private static final String SERVICO = "ServicoMensagens";
	private static final String SERVIDOR = "127.0.0.1";
	
	private static long clienteA = 0L;
	private static long clienteB = 0L;
	private long idConfirmou;
	private static boolean vez = false;
 
	
	
	
	public ServicoLista() throws RemoteException {
		super();
	}
	

	
	public static String getURI() {
		return "//"+SERVIDOR+"/"+SERVICO;
	}

	
	@Override
	public void confirmar(long id) throws RemoteException {
		this.idConfirmou = id;
	}
	
	@Override
	public long idConfirmou() throws RemoteException {
		return this.idConfirmou;
	}

	@Override
	public void menu(long id) throws RemoteException {

	}
	
	@Override
	public void teste() throws RemoteException {
		System.out.println("ID Cliente A: "+clienteA+"\t"+"ID Cliente B: "+clienteB);
		System.out.println("\n"+vez);
	}

	@Override
	public void addItem(String nome, int quant) throws RemoteException {
		Item item = new Item();
		item.setNome(new StringBuilder(nome));
		item.setQuant(quant);
		listaCasamento.add(item);
	}
	
	@Override
	public List<Item> getLista() throws RemoteException {
		return listaCasamento;
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
		if (!clientesCriados()) {
			if (clienteA == 0L) {
				clienteA = id;
			} else {
				clienteB = id;
			}				
		}
	}
	
	
	private boolean clientesCriados() {
		return (clienteA != 0L && clienteB != 0L);
	}
	
	@Override
	public boolean minhaVez() throws RemoteException {
		return vez;
	}
	
	@Override
	public void trocaVez() throws RemoteException {
		vez = !vez;
	}
	
}
