package irani.entities;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import irani.util.BigDecimalConverter;
import irani.util.LocalDateTimeConverter;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PrintData {


    @CsvBindByName(column = "Login")
    private String login;

    @CsvBindByName(column = "Dominio")
    private String dominio;

    @CsvBindByName(column = "Nome")
    private String nome;

    @CsvBindByName(column = "Centro Custo")
    private String centroCusto;

    @CsvBindByName(column = "Nome Dispositivo")
    private String nomeDispositivo;

    @CsvBindByName(column = "Modelo Dispositivo")
    private String modeloDispositivo;

    @CsvBindByName(column = "Serial Dispositivo")
    private String serialDispositivo;

    @CsvBindByName(column = "Endereco Dispositivo")
    private String enderecoDispositivo;

    @CsvBindByName(column = "Depto Dispositivo")
    private String departamentoDispositivo;

    @CsvBindByName(column = "Computador Origem")
    private String computadorOrigem;

    @CsvBindByName(column = "Servidor Impressao")
    private String servidorImpressao;

    @CsvBindByName(column = "Endereco Servidor")
    private String enderecoServidor;

    @CsvBindByName(column = "Servidor OS")
    private String servidorOs;

    @CsvBindByName(column = "Fila Impressao")
    private String filaImpressao;

    @CsvBindByName(column = "Driver Impressao")
    private String driverImpressao;

    @CsvBindByName(column = "Port Name")
    private String portName;

    @CsvBindByName(column = "Tipo Trabalho")
    private String tipoTrabalho;

    @CsvBindByName(column = "Confianca")
    private String confianca;

    @CsvBindByName(column = "Documento Nome")
    private String documentoNome;

    @CsvBindByName(column = "Aplicativo Impressao")
    private String aplicativoImpressao;

    @CsvCustomBindByName(column = "Data Impressao", converter = LocalDateTimeConverter.class)
    private LocalDateTime dataImpressao;

    @CsvBindByName(column = "Papel")
    private String papel;

    @CsvBindByName(column = "Resolucao")
    private Integer resolucao;

    @CsvBindByName(column = "Duplex")
    private Integer duplex;

    @CsvBindByName(column = "Paginas Originais")
    private Integer paginasOriginais;

    @CsvBindByName(column = "Copias")
    private Integer copias;

    @CsvBindByName(column = "Total Paginas Cor")
    private Integer totalPaginasCor;

    @CsvBindByName(column = "Total Paginas Mono")
    private Integer totalPaginasMono;

    @CsvBindByName(column = "Total Impresso")
    private Integer totalImpresso;

    @CsvCustomBindByName(column = "Custo", converter = BigDecimalConverter.class)
    private BigDecimal custo;

}
