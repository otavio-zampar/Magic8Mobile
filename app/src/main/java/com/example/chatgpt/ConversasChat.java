package com.example.chatgpt;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


public class ConversasChat extends AppCompatActivity {

    private View sidebarContainer, imgIconBorder;
    private ImageButton imgOpen, imgIcon;

    private TextView titulo, txtEdNome, txtEdSenha;
    private EditText editNome, editSenha, confirmarSenha;
    private AppCompatButton confirmaEdit;
    private boolean sidebarOpen = false;
    private static final int REQUEST_PERMISSION = 1;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    private int Nconversas = 0;
//    private MediaPlayer bckg;
//    private Bitmap yourSelectedImage;

    private void getSidebarIds(){
        imgOpen = findViewById(R.id.imgSidebar);
        imgIconBorder = findViewById(R.id.imgIconBorder);
        sidebarContainer = findViewById(R.id.sidebarContainer);
        titulo = findViewById(R.id.titulo);
        txtEdNome = findViewById(R.id.textEditNome);
        txtEdSenha = findViewById(R.id.textEditSenha);
        confirmarSenha = findViewById(R.id.ConfirmarSenha);
        confirmaEdit = findViewById(R.id.BTNconfirmarEdit);
        editSenha = findViewById(R.id.editSenha);
        editNome = findViewById(R.id.editNome);
        imgIcon = findViewById(R.id.imgIcon);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String userEMail = intent.getStringExtra("userEmail");
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_conversas_chat);
        AppCompatButton btnNovaConversa = findViewById(R.id.btnAddConversa);
        TextView userName = findViewById(R.id.textviewuser);
        ImageButton imgAdd = findViewById(R.id.add);
        ImageButton imgDel = findViewById(R.id.trash);
//        bckg = MediaPlayer.create(getApplicationContext(), R.raw.wii_shop_bossa_nova);
//        bckg.setVolume(0.2F, 0.2F);
//        bckg.setLooping(true);
//        bckg.start();

        ImageButton imgEdit = findViewById(R.id.edit);
        LinearLayout parentLayout = findViewById(R.id.parentLayout);
        try {
            getSidebarIds();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Uh Oh, algo deu errado na sidebar!", Toast.LENGTH_SHORT).show();
        }
        DBHelper DB = new DBHelper(getApplicationContext());
        int UserID = DB.getID(userEMail);

        if (!userEMail.isEmpty()) {
            userName.setText(DB.getUsername(userEMail));
        } else{
            userName.setText("False ID");
        }

        // for every row in DB create a button and increment to Nconversas with name "conversas(nome)"
        for (int id = 0; id < DB.getConversaRows(UserID); id = id+1){
            AppCompatButton NewButton = findViewById(createButton(btnNovaConversa, parentLayout));
            NewButton.setText(DB.getCvsName(UserID, id));
            final int id2 = id;
            NewButton.setOnClickListener(view -> {
                Intent i = new Intent(getApplicationContext(), chat.class);
                i.putExtra("ConvID", id2);
                i.putExtra("UserID", UserID);
                startActivity(i);
            });

            Nconversas = id+1;
//            Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
        }

        imgEdit.setOnClickListener(view -> {
            Toast.makeText(this, "AAAAAAAA", Toast.LENGTH_SHORT).show();
        });

        imgAdd.setOnClickListener(v -> {
            if (Nconversas <= 8) {
                Nconversas = Nconversas+1;
                AppCompatButton NewButton = findViewById(createButton(btnNovaConversa, parentLayout));
                NewButton.setOnClickListener(view -> {

                    Nconversas= Nconversas+1;
                    if(!(DB.insertConversas(UserID, ("Conversa #" + Nconversas)))){
                        Toast.makeText(this, "Não foi possível criar a conversa", Toast.LENGTH_SHORT).show();
                    }

                    Intent i = new Intent(getApplicationContext(), chat.class);
                    i.putExtra("ConvID", DB.getConvID(("Conversa #" + Nconversas)));
                    i.putExtra("UserID", UserID);
                    startActivity(i);
                });
            }else{
                Toast.makeText(imgAdd.getContext(), "Limite de 9 conversas atingido!", Toast.LENGTH_SHORT).show();
            }
        });

        imgOpen.setOnClickListener(view -> openSidebar());

        imgDel.setOnClickListener(view -> {
            if (DB.nuke(3)){
                Toast.makeText(imgDel.getContext(), "Apagado todas as conversas", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(imgDel.getContext(), "Erro ao deletar Todas as Linhas do banco... YAY", Toast.LENGTH_SHORT).show();
            }
        });

        btnNovaConversa.setOnClickListener(view -> {

            Intent i = new Intent(getApplicationContext(), chat.class);

            Nconversas= Nconversas+1;
            if(!(DB.insertConversas(UserID, ("Conversa #" + Nconversas)))){
                Toast.makeText(this, "Não foi possível criar a conversa", Toast.LENGTH_SHORT).show();
            }

            // aqui nao deveria estar retornando -1
            i.putExtra("ConvID", DB.getConvID(("Conversa #" + Nconversas)));
            Toast.makeText(this, String.valueOf(DB.getConvID(("Conversa #" + Nconversas))), Toast.LENGTH_SHORT).show();
            i.putExtra("UserID", UserID);
            startActivity(i);

        });

        confirmaEdit.setOnClickListener(view -> {
            if (!editSenha.getText().toString().equals("") && !confirmarSenha.getText().toString().equals("")) {
                if (DB.checkSenha(confirmarSenha.getText().toString())) {
                    DB.editSenha(UserID, editSenha.getText().toString());
                    Toast.makeText(getApplicationContext(), "Senha editada com sucesso.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível editar a senha", Toast.LENGTH_SHORT).show();
                }
            }

            if (!editNome.getText().toString().equals("")){
                DB.editUN(UserID, editNome.getText().toString());
                Toast.makeText(getApplicationContext(), "Usuário editado com sucesso.", Toast.LENGTH_SHORT).show();
            }
        });

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                openGallery();
            } else {
                Toast.makeText(this, "A permissão é necessária para mudar a imagem.", Toast.LENGTH_SHORT).show();
            }
        });

        // Create the ActivityResultLauncher for launching the gallery intent
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                Uri selectedImageUri = data.getData();
                imgIcon.setImageURI(selectedImageUri);

            }
        });

        imgIcon.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        bckg.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        bckg.stop();
    }

    @SuppressLint({"ResourceType", "DefaultLocale"})
    protected int createButton(AppCompatButton btnNovaConversa, LinearLayout parentLayout) {
        ContextThemeWrapper newContext = new ContextThemeWrapper(getApplicationContext(), R.style.BTNComponents);
        AppCompatButton newButton = new AppCompatButton(newContext);
        newButton.setId(View.generateViewId());
        newButton.setText(String.format("Nova Conversa #%d", Nconversas));
        newButton.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        int dp25 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        int dp5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        layoutParams.setMargins(dp25, dp5, dp25, 0);
        newButton.setLayoutParams(layoutParams);
        parentLayout.addView(newButton, 0);
        LinearLayout.LayoutParams newLayoutParams = (LinearLayout.LayoutParams) btnNovaConversa.getLayoutParams();
        newLayoutParams.topMargin = dp5;
        btnNovaConversa.setLayoutParams(newLayoutParams);

        return newButton.getId();
    }

    public void openSidebar() {
        if (sidebarOpen) {
            sidebarContainer.setVisibility(View.VISIBLE);
            closeSidebar();
        } else {
            sidebarContainer.setVisibility(View.INVISIBLE);
            openSidebar2();
        }
    }

    private void openSidebar2() {
        setVisibility(1); // changes everything in the sidebar to visible
        sidebarContainer.animate().translationX(sidebarContainer.getWidth()).setDuration(300).setListener(null);
        imgOpen.animate().translationX(sidebarContainer.getWidth()).setDuration(300).rotation(90).setListener(null);
        imgIconBorder.animate().translationX(sidebarContainer.getWidth()).setDuration(300).rotation(90).setListener(null);
        titulo.animate().translationX(sidebarContainer.getWidth()).setDuration(300).setListener(null);
        editNome.animate().translationX(sidebarContainer.getWidth()).setDuration(300).setListener(null);
        editSenha.animate().translationX(sidebarContainer.getWidth()).setDuration(300).setListener(null);
        confirmarSenha.animate().translationX(sidebarContainer.getWidth()).setDuration(300).setListener(null);
        txtEdNome.animate().translationX(sidebarContainer.getWidth()).setDuration(300).setListener(null);
        txtEdSenha.animate().translationX(sidebarContainer.getWidth()).setDuration(300).setListener(null);
        confirmaEdit.animate().translationX(sidebarContainer.getWidth()).setDuration(300).setListener(null);
        imgIcon.animate().translationX(sidebarContainer.getWidth()).setDuration(300).setListener(null);
        sidebarOpen = true;
    }

    private void closeSidebar() {
        sidebarContainer.animate()
                .translationX(-sidebarContainer.getWidth())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setVisibility(0); // changes everything in the sidebar to gone
                        sidebarOpen = false;
                    }
                });
        imgOpen.animate().translationX(0).setDuration(300).rotation(270).setListener(null);
        imgIconBorder.animate().translationX(0).setDuration(300).rotation(270).setListener(null);
        titulo.animate().translationX(-sidebarContainer.getWidth()).setDuration(300).setListener(null);
        editNome.animate().translationX(-sidebarContainer.getWidth()).setDuration(300).setListener(null);
        editSenha.animate().translationX(-sidebarContainer.getWidth()).setDuration(300).setListener(null);
        confirmarSenha.animate().translationX(-sidebarContainer.getWidth()).setDuration(300).setListener(null);
        txtEdSenha.animate().translationX(-sidebarContainer.getWidth()).setDuration(300).setListener(null);
        txtEdNome.animate().translationX(-sidebarContainer.getWidth()).setDuration(300).setListener(null);
        confirmaEdit.animate().translationX(-sidebarContainer.getWidth()).setDuration(300).setListener(null);
        imgIcon.animate().translationX(-sidebarContainer.getWidth()).setDuration(300).setListener(null);

    }

    private void setVisibility(int view){
        int change = -1;
        switch (view){
            case 0:
                change = View.GONE;
                break;
            case 1:
                change = View.VISIBLE;
                break;
            default:
                break;
        }
        sidebarContainer.setVisibility(change);
        imgIconBorder.setVisibility(change);
        titulo.setVisibility(change);
        editNome.setVisibility(change);
        editSenha.setVisibility(change);
        confirmaEdit.setVisibility(change);
        confirmarSenha.setVisibility(change);
        txtEdSenha.setVisibility(change);
        txtEdNome.setVisibility(change);
        imgIcon.setVisibility(change);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

}
