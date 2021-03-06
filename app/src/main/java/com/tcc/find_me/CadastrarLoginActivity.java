package com.tcc.find_me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.tcc.find_me.config.ConfiguracaoFirebase;
import com.tcc.find_me.model.Usuario;

public class CadastrarLoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Switch switchTipoUsuario;
    private FirebaseAuth autenticacao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_login);

        campoEmail = findViewById(R.id.editEmailCadastro);
        campoSenha = findViewById(R.id.editSenhaCadastro);
        switchTipoUsuario = findViewById(R.id.switchTipoUsuario);

    }
    public void validarCadastroUsuario(View view){
        //Recuperar textos dos campos
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if (!textoEmail.isEmpty()){
            if(!textoSenha.isEmpty()){

                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);
                usuario.setTipo(verificaTipoUsuario());


                cadastrarUsuario(usuario);

            }else {
                Toast.makeText(getApplicationContext(),"Preencha a Senha !",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Preencha o Email !",Toast.LENGTH_LONG).show();
        }
    }

    //Verifica o tipo de usuario
    public String verificaTipoUsuario(){ return switchTipoUsuario.isChecked() ?  "P"  :  "C"  ; }

    public void cadastrarUsuario(Usuario usuario) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"Sucesso Ao Cadastrar Cliente",Toast.LENGTH_LONG).show();
                    String idUsuario = task.getResult().getUser().getUid();
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
                    Toast.makeText(getApplicationContext(),"Sucesso Ao Cadastrar Cliente",Toast.LENGTH_LONG).show();
                }else{

                    //pegando Exceptions do link https://firebase.google.com/docs/reference/android/com/google/firebase/auth/package-summary?authuser=0
                    // para verificar se o usuario digitou o email ou a senha errada

                    String excessao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidUserException e){ //excessao para verificar se o usuario existe no banco
                        excessao = "Usuario Nao Cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){  //excessao para ver se o email e senha estao corretos
                        excessao = "Email ou Senha Incorretos";
                    }catch (Exception e) {
                        excessao = "Erro ao Logar Na Conta !";
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),excessao,Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}

/*
                    if (task.isSuccessful()){


                    String idUsuario = task.getResult().getUser().getUid();
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
                    Toast.makeText(getApplicationContext(),"Sucesso Ao Cadastrar Cliente",Toast.LENGTH_LONG).show();
                }else{

                    //pegando Exceptions do link https://firebase.google.com/docs/reference/android/com/google/firebase/auth/package-summary?authuser=0
                    // para verificar se o usuario digitou o email ou a senha errada

                    String excessao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidUserException e){ //excessao para verificar se o usuario existe no banco
                        excessao = "Usuario Nao Cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){  //excessao para ver se o email e senha estao corretos
                        excessao = "Email ou Senha Incorretos";
                    }catch (Exception e) {
                        excessao = "Erro ao Logar Na Conta !";
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),excessao,Toast.LENGTH_LONG).show();
                }
 */