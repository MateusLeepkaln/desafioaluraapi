package br.com.alura.desafioalura.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}