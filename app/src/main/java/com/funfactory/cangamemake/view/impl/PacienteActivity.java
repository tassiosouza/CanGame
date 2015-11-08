package com.funfactory.cangamemake.view.impl;

import java.util.Calendar;
import java.util.Date;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.IPacientePresenter;
import com.funfactory.cangamemake.presenter.impl.PacientePresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.util.DateUtil;
import com.funfactory.cangamemake.util.MaskUtil;
import com.funfactory.cangamemake.util.PhotoUtil;
import com.funfactory.cangamemake.view.IEdicaoActivity;
import com.funfactory.cangamemake.view.adapter.FiltroNome;

@ContentView(R.layout.activity_edicao_paciente)
public class PacienteActivity extends GenericActivity implements IEdicaoActivity {

    /**
     * Componentes da View
     */
    @InjectView(R.id.text_view_data_nasc)
    private TextView           txtDataNasc;

    @InjectView(R.id.nomePaciente)
    private TextView           txtNomePaciente;

    @InjectView(R.id.nomePai)
    private TextView           txtNomePai;

    @InjectView(R.id.nomeMae)
    private TextView           txtNomeMae;

    @InjectView(R.id.contatoPai)
    private EditText           txtContatoPai;

    @InjectView(R.id.contatoMae)
    private EditText           txtContatoMae;

    @InjectView(R.id.sexo_masculino)
    private RadioButton        sexoMasculino;

    @InjectView(R.id.sexo_feminino)
    private RadioButton        sexoFeminino;

    @InjectView(R.id.photo_image)
    private ImageView          photoImage;
    
    private ImageButton        buttonPhotoImage;

    /**
     * Presenters
     */
    private IPacientePresenter mPacientePresenter = new PacientePresenter();

    /**
     * Components de UI
     */
    private PopupMenu          popupMenu;

    private Boolean            isSexoMasculino;

    private Date               selectedDate;
    private Calendar           calendar;
    private int                year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setParentDependencies(this, mPacientePresenter);
        setContentView(R.layout.activity_edicao_paciente);

        /*
         * Set the view
         */
        mPacientePresenter.setView(this);

        txtDataNasc = (TextView) findViewById(R.id.text_view_data_nasc);
        txtNomePaciente = (TextView) findViewById(R.id.nomePaciente);
        txtNomePai = (TextView) findViewById(R.id.nomePai);
        txtNomeMae = (TextView) findViewById(R.id.nomeMae);
        txtContatoPai = (EditText) findViewById(R.id.contatoPai);
        txtContatoMae = (EditText) findViewById(R.id.contatoMae);
        sexoMasculino = (RadioButton) findViewById(R.id.sexo_masculino);
        sexoFeminino = (RadioButton) findViewById(R.id.sexo_feminino);
        photoImage = (ImageView) findViewById(R.id.photo_image);
        photoImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        buttonPhotoImage = (ImageButton) findViewById(R.id.btnCallPhoto);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
        calendar.set(year, month + 1, day);

        configureActions();

        txtContatoPai.addTextChangedListener(MaskUtil.insert("(##)#########", txtContatoPai));
        txtContatoMae.addTextChangedListener(MaskUtil.insert("(##)#########", txtContatoMae));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
    }

    private void configureActions() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);

        FiltroNome filtroNome = new FiltroNome();
        InputFilter[] filters = new InputFilter[] { filtroNome };

        txtNomePaciente.setFilters(filters);
        txtNomePai.setFilters(filters);
        txtNomeMae.setFilters(filters);
    }

    public void onOptionSelected(View view) {
        mPacientePresenter.onOptionSeleted(view.getId());
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if (txtDataNasc != null && selectedDate == null){
    		txtDataNasc.setText(R.string.date_selected);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.salvar, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            salvar();
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void salvar() {

        String nome = txtNomePaciente.getText().toString();
        String nomePai = txtNomePai.getText().toString();
        String nomeMae = txtNomeMae.getText().toString();
        String contatoPai = txtContatoPai.getText().toString();
        String contatoMae = txtContatoMae.getText().toString();

        mPacientePresenter
                .salvarPaciente(nome, nomePai, nomeMae, contatoPai, contatoMae, selectedDate, isSexoMasculino);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
        case R.id.sexo_masculino:
            if (checked)
                isSexoMasculino = true;
            break;
        case R.id.sexo_feminino:
            if (checked)
                isSexoMasculino = false;
            break;
        }
    }

    @Override
    public IGenericActivity getGenericMethods() {
        return this;
    }

    @Override
    public void inserirConteudo(Object object) {

        if (object != null) {
            Paciente paciente = (Paciente) object;

            txtDataNasc.setText(DateUtil.format(paciente.getDataNascimento()));
            selectedDate = paciente.getDataNascimento();
            txtNomePaciente.setText(paciente.getNome());
            txtNomePai.setText(paciente.getNomePai());
            txtNomeMae.setText(paciente.getNomeMae());
            txtContatoPai.setText(paciente.getContatoPai());
            txtContatoMae.setText(paciente.getContatoMae());

            if (paciente.getImagePath() != null && !paciente.getImagePath().equals("")) {

                Bitmap btmp = PhotoUtil.getInstance().createBitmap(paciente.getImagePath());

                if (btmp == null) {
                    btmp = PhotoUtil.getInstance().decodeBitmap(paciente.getImagePath());
                }
                if (btmp != null) {
                    photoImage.setImageBitmap(btmp);
                }
            }

            if (paciente.getSexo() != null) {
                if (paciente.getSexo().equalsIgnoreCase(Constants.SEXO_FEMINIMO)) {
                    sexoFeminino.setChecked(true);
                    sexoMasculino.setChecked(false);
                    isSexoMasculino = false;
                } else if (paciente.getSexo().equalsIgnoreCase(Constants.SEXO_MASCULINO)) {
                    sexoMasculino.setChecked(true);
                    sexoFeminino.setChecked(false);
                    isSexoMasculino = true;
                }
            }

            if (paciente.getDataNascimento() != null) {

                Date date = paciente.getDataNascimento();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            DatePickerDialog dialog = new DatePickerDialog(this, myDateListener, year, month, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

                                                                  @Override
                                                                  public void onDateSet(DatePicker arg0, int arg1,
                                                                          int arg2, int arg3) {
                                                                      showDate(arg1, arg2 + 1, arg3);
                                                                      selectedDate = new Date(arg0.getCalendarView()
                                                                              .getDate());
                                                                      txtDataNasc
                                                                              .setText(DateUtil.format(selectedDate));
                                                                  }
                                                              };

    private void showDate(int year, int month, int day) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
    }

    @Override
    public void enableMenuContext(boolean enable) {

        popupMenu = new PopupMenu(PacienteActivity.this, photoImage);
        popupMenu.getMenuInflater().inflate(R.menu.foto, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mPacientePresenter.onOptionSeleted(item.getItemId());
                return true;
            }
        });
        
        popupMenu = new PopupMenu(PacienteActivity.this, buttonPhotoImage);
        popupMenu.getMenuInflater().inflate(R.menu.foto, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mPacientePresenter.onOptionSeleted(item.getItemId());
                return true;
            }
        });

        if (enable) {
            photoImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });
            
            buttonPhotoImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });

        } else {
            photoImage.setOnClickListener(null);
            buttonPhotoImage.setOnClickListener(null);
        }

    }

    @Override
    public void configComponents(boolean edicao, Object object) {
        // TODO Auto-generated method stub
    }

    @Override
    public void saveScreen() {

        String nome = txtNomePaciente.getText().toString();
        String nomePai = txtNomePai.getText().toString();
        String nomeMae = txtNomeMae.getText().toString();
        String contatoPai = txtContatoPai.getText().toString();
        String contatoMae = txtContatoMae.getText().toString();

        mPacientePresenter.salvarTela(nome, nomePai, nomeMae, contatoPai, contatoMae, selectedDate, isSexoMasculino);
    }
}
