package br.com.puc.basenosql;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.core.GraphDatabase;

import br.com.puc.basenosql.entity.Categoria;
import br.com.puc.basenosql.entity.Pessoa;
import br.com.puc.basenosql.entity.Produto;
import br.com.puc.basenosql.repository.CategoriaRepository;
import br.com.puc.basenosql.repository.PessoaRepository;
import br.com.puc.basenosql.repository.ProdutoRepository;

@Configuration
@EnableNeo4jRepositories(basePackages = "br.com.puc.basenosql")
public class AvaliacaoPerformance extends Neo4jConfiguration implements
		CommandLineRunner {

	private Set<Pessoa> pessoas = new HashSet<Pessoa>();
	private Set<Produto> produtos = new HashSet<Produto>();
	private int qtdUF = 0;
	private int qtdVendas = 0;
	private int qtdCompas = 4;
	
	public AvaliacaoPerformance() {
		setBasePackage("br.com.puc.basenosql");
	}

	@Bean
	GraphDatabaseService graphDatabaseService() {
		return new GraphDatabaseFactory()
				.newEmbeddedDatabase("/var/lib/neo4j/data/graph.db");
	}

	@Autowired
	PessoaRepository pessoaRepository;

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	CategoriaRepository categoriaRepository;

	@Autowired
	GraphDatabase graphDatabase;

	public void run(String... args) throws Exception {
		
		long antes = System.currentTimeMillis();    
        processarInformacoes();
		System.out.printf("O programa executou em %d milissegundos.%n", System.currentTimeMillis() - antes);
		
	}
	
	private void _carregarInformacoesIniciais() {
		Transaction tx = graphDatabase.beginTx();
		try {
			
			/** Categorias ---------------------------------------------------- */
			Categoria catInformatica = new Categoria();
			catInformatica.setDescricao("Informatica");
			catInformatica = categoriaRepository.save(catInformatica);

			Categoria catCelulares = new Categoria();
			catCelulares.setDescricao("Celulares");
			catCelulares = categoriaRepository.save(catCelulares);

			Categoria catJogos = new Categoria();
			catJogos.setDescricao("Jogos");
			catJogos = categoriaRepository.save(catJogos);

			Categoria catLivros = new Categoria();
			catLivros.setDescricao("Livros");
			catLivros = categoriaRepository.save(catLivros);

			/** Produtos ---------------------------------------------------- */
			Produto produto;
			for (int indiceProduto = 0; indiceProduto < 5000; indiceProduto++) {
				produto = new Produto();
				produto.setCategoria(catInformatica);
				produto.setNome("Mouse Modelo: " + (indiceProduto + 1));
				produto = produtoRepository.save(produto);
				produtos.add(produto);

				produto = new Produto();
				produto.setCategoria(catCelulares);
				produto.setNome("Aparelho Celular Modelo: " + (indiceProduto + 1));
				produto = produtoRepository.save(produto);
				produtos.add(produto);

				produto = new Produto();
				produto.setCategoria(catJogos);
				produto.setNome("Jogo de Aventura Versao: " + (indiceProduto + 1));
				produto = produtoRepository.save(produto);
				produtos.add(produto);

				produto = new Produto();
				produto.setCategoria(catLivros);
				produto.setNome("Livro de Informatica Edicao: "
						+ (indiceProduto + 1));
				produto = produtoRepository.save(produto);
				produtos.add(produto);
			}

			Random random = new Random(System.currentTimeMillis());

			/**
			 * Pessoas (Compradores e Vendedores)
			 * -------------------------------------------
			 */
			Pessoa pessoa;
			for (int indicePessoa = 0; indicePessoa < 1500; indicePessoa++) {

				String[] nomes = new String[] { "Sergio", "Felipe", "Vanessa",
						"Rudson", "Erika", "Everton", "Carina", "Carlos", "Rafael",
						"Alberto", "Alessandra", "Patracia", "Monica", "Fernanda",
						"Marcia" };

				String[] sobreNomes = new String[] { "Silva", "Barbosa", "Santos",
						"Dutra", "Oliveira", "Pinheiro", "Soares", "Carvalho",
						"Andrade", "Dias" };

				String nomePessoa = nomes[random.nextInt(nomes.length)] + " "
						+ sobreNomes[random.nextInt(sobreNomes.length)];
				String emailPessoa = nomePessoa.toLowerCase().replace(" ", ".")
						+ "@pucminas.com.br";
				String UF = getUf();

				pessoa = new Pessoa();
				pessoa.setNome(nomePessoa);
				pessoa.setEmail(emailPessoa);
				pessoa.setUF(UF);
				pessoas.add(pessoa);
			}
			
			tx.success();
		} finally {
			tx.close();
		}
	}
	
	private void processarInformacoes() {
		for(int i = 1; i<= 5; i++){
			System.out.printf(">>>>>>>>>>INICIO EXECUCAO %d° TESTE<<<<<<<<<<%n", i);
			_processarInformacoes();
			limparInformacoes();
			System.out.printf(">>>>>>>>>>FIM EXECUCAO %d° TESTE<<<<<<<<<<%n", i);
		}
	}
	
	private void _processarInformacoes() {
		int qtdOperacoes = 2500;
		int qtdAuxConsulta = 0;
		_carregarInformacoesIniciais();
		long antes = System.currentTimeMillis();
		while(qtdOperacoes <= 40000){
			_carregarInformacoes(qtdOperacoes);
			System.out.printf("Tempo inserçao %d operacoes: %d milissegundos.%n", qtdOperacoes, System.currentTimeMillis() - antes);
			antes = System.currentTimeMillis();
			consultarInformacoes();
			System.out.printf("Tempo consulta %d operacoes: %d milissegundos.%n", qtdOperacoes+qtdAuxConsulta, System.currentTimeMillis() - antes);
			antes = System.currentTimeMillis();
			qtdAuxConsulta += qtdOperacoes;
			qtdOperacoes += qtdOperacoes;
		}
	}
	
	private void limparInformacoes() {
		limparBase();
		pessoas = new HashSet<Pessoa>();
		produtos = new HashSet<Produto>();
	}
	
	private void limparBase() {
		long antes = System.currentTimeMillis();
		ExecutionEngine engine = new ExecutionEngine(graphDatabaseService());
		
		Transaction tx = graphDatabase.beginTx();
		try {
			engine.execute("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r;");
			tx.success();
		} finally {
			tx.close();
		}
		System.out.printf("Tempo remoçao: %d milissegundos.%n", System.currentTimeMillis() - antes);
	}
	
	private void _carregarInformacoes(int qtdOperacoes) {
		Transaction tx = graphDatabase.beginTx();
		try {
			/** Operacoes de compra e venda */
			for (long indiceOperacao = 0; indiceOperacao < qtdOperacoes; indiceOperacao++) {
	
				/** Operacao de Compra */
				Pessoa comprador = selecionaPessoaAleatorio(pessoas);
	
				int qtdeCompras = getQtdCompras();
				for (int indiceCompra = 0; indiceCompra < qtdeCompras; indiceCompra++) {
					comprador.compraUm(selecionaProdutoAleatorio(produtos),
							"Operacao de compra (" + (indiceCompra + 1) + ")");
				}
				pessoaRepository.save(comprador);
	
				/** Operacao de Venda */
				Pessoa vendedor = selecionaPessoaAleatorio(pessoas);
	
				int qtdeVendas = getQtdVendas();
				for (int indiceVenda = 0; indiceVenda < qtdeVendas; indiceVenda++) {
					vendedor.vendeUm(selecionaProdutoAleatorio(produtos),
							"Operacao de venda (" + indiceVenda + ")");
				}
				pessoaRepository.save(vendedor);
			}
			tx.success();
		} finally {
			tx.close();
		}
	}

	
	private String getUf() {
		String[] UFs = new String[] { "SP", "RJ", "PR", "MG" };
		String uf = UFs[qtdUF++];
		if(qtdUF == UFs.length){
			qtdUF = 0;
		}
		return uf;
	}
	
	private int getQtdVendas() {
		int qtdVendas = this.qtdVendas++;
		if(this.qtdVendas > 4){
			this.qtdVendas = 0;
		}
		return qtdVendas;
	}
	
	private int getQtdCompras() {
		int qtdCompas = this.qtdCompas--;
		if(this.qtdCompas < 0){
			this.qtdCompas = 4;
		}
		return qtdCompas;
	}

	/**
	 * Seleciona produto aleatoriamente
	 * 
	 * @param produtos
	 * @return
	 */
	private Produto selecionaProdutoAleatorio(Set<Produto> produtos) {
		Random random = new Random(System.currentTimeMillis());
		return (Produto) produtos.toArray()[random.nextInt(produtos.size())];
	}

	/**
	 * Seleciona pessoa aleatoriamente
	 * 
	 * @param pessoas
	 * @return
	 */
	private Pessoa selecionaPessoaAleatorio(Set<Pessoa> pessoas) {
		Random random = new Random(System.currentTimeMillis());
		return (Pessoa) pessoas.toArray()[random.nextInt(pessoas.size())];
	}

	public void consultarInformacoes() {
		ExecutionEngine engine = new ExecutionEngine(graphDatabaseService());
		
		Transaction tx = graphDatabase.beginTx();
		try {
			
			engine.execute("MATCH pessoa-[:COMPROU_UM]->produto-[:PERTENCE_A_UMA]->categoria "
							+ "WHERE NOT (pessoa-[:VENDEU_UM]->produto) AND categoria.descricao='Livros' AND pessoa.UF='MG' "
							+ "RETURN pessoa.nome, pessoa.email, pessoa.UF, categoria.descricao, count(*); ");
			
			tx.success();
		} finally {
			tx.close();
		}
	}
	
	/*public void consultarInformacoes() {
		ExecutionEngine engine = new ExecutionEngine(graphDatabaseService());
		
		Transaction tx = graphDatabase.beginTx();
		try {
			
			engine.execute("MATCH pessoa-[:VENDEU_UM]->produto-[:PERTENCE_A_UMA]->categoria "
							+ "AND categoria.descricao='Celulares' AND pessoa.nome='Alessandra Soares' "
							+ "RETURN count(*); ");
			
			tx.success();
		} finally {
			tx.close();
		}
	}*/
	
	/*public void consultarInformacoes() {
		ExecutionEngine engine = new ExecutionEngine(graphDatabaseService());
		
		Transaction tx = graphDatabase.beginTx();
		try {
			
			engine.execute("MATCH produto-[:PERTENCE_A_UMA]->categoria "
							+ "AND categoria.descricao='Jogos'"
							+ "RETURN count(*); ");
			
			tx.success();
		} finally {
			tx.close();
		}
	}*/

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AvaliacaoPerformance.class, args);
	}

}
