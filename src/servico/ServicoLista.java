package servico;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
	private String nomeA;
	private String nomeB;
	private boolean finalizou = false;
	private boolean liberacao = false;

	private String ipServidor1 = "127.0.0.1";
	private String ipServidor2 = "192.168.15.7";
	
	
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
	public boolean checkConfirmacao() {
		return this.turno.clienteAConfirmado && this.turno.clienteBConfirmado;
	}
	
	@Override
	public boolean liberarProxEtapa() throws RemoteException {
		return this.finalizou;
	}
	
	@Override
	public boolean possoProsseguir(long clienteID) throws RemoteException {
		if (clienteID == clienteB) {
			this.finalizou = true;
			return true;
		}
		return false;
	}
	
	@Override
	public void menu(long id) throws RemoteException {

	}
	
	@Override
	public void setIpServidor(String ip) {
		if (this.ipServidor1 == null) {
			this.ipServidor1 = ip;
		} else if(this.ipServidor2 == null) {
			this.ipServidor2 = ip;
		}
	}
	
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
	public String getNomeCliente(long id) throws RemoteException {
		if (id == clienteA) {
			return "Cliente 1";
		} else {
			return "Cliente 2";
		}
	}
	
	@Override
	public void mudarTurno(long clienteID) throws RemoteException {
		if(clienteID == clienteA)
			this.turno.setIdUsuario(clienteB);
		else if(clienteID == clienteB)
			this.turno.setIdUsuario(clienteA);
	}
	
	
	@Override
	public boolean podeFinalizar(long clienteID) {
		return clienteID == clienteA;
	}
	
	@Override
	public boolean checkLiberacao() throws RemoteException {
		return this.liberacao;
	}
	
	
	@Override
	public void liberarOutroCliente() throws RemoteException {
		this.liberacao = true;		
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
	
	@Override
	public void setLista(Iterable<Item> lista) throws RemoteException {
		this.listaCasamento = (List<Item>) lista;
	}

	@Override
	public void updateLista(Iterable<Item> lista) {
		this.listaCasamento = (List<Item>) lista;
	}
	
	@Override
	public void showLista(Iterable<Item> lista) {
		System.out.println("LISTA: \n");
		
		int i = 1;
		for (Item it : lista) {
			System.out.println("Código: "+i+ "\tNome: "+it.getNome().toString()+"\tQuantidade: "+it.getQuant());
			i++;
		}
	}
	
	@Override
	public void enviaParaOutroServidor(Iterable<Item> lista) throws RemoteException {

		try {
			ServicoListaInterface servicoLista2 = (ServicoListaInterface) Naming.lookup("//"+ipServidor2+"/"+"servico");
			servicoLista2.updateLista(lista);
			servicoLista2.showLista(lista);				
		} catch (MalformedURLException | NotBoundException e) {
			e.printStackTrace();
			
		}
	}
	
	@Override
	public void resetVars() throws RemoteException{
		clienteA = 0L;
		clienteB = 0L;
		this.listaCasamento = new ArrayList<Item>();
		this.turno = new Turno();
		this.finalizou = false;
		this.liberacao = false;
		System.out.println("Resetou as variáveis");
	}


	@Override
	public long idConfirmou() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

}
