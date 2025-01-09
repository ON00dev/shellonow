cfg.Dark
   //AntiCrack
   //Version 0.02
   //Author: Base64s
   //Detect Jasi Patcher
var jasi = app.IsAppInstalled("com.android.vendinf");
//Detect Su
var supersu = app.FileExists("/system/xbin/su");
//Detect Lucky Patcher
var lucky = app.FileExists("/data/local/tmp/lp_utils");
//Detect Magisk
var magisk = app.FileExists("/sbin/su" );
//Detect Virtual Space
//var virtual
//Detect Xposed
var xposed = app.FileExists( "/system/xposed.prop" )
//Get list of installed apps
var apps = app.GetActivities();

function Alert()
{
   //Alert if Root permission(su) & Hacker softwares detected
   app.Alert(
      "Hacker software detected!\nJasi Patcher: "+jasi+"\nLucky Patcher: "+lucky+"\nRoot permission: "+supersu+"\nMagisk: "+magisk+"\nXposed: "+xposed+"\nPlease delete Hacker software!",
      "AntiCrack");
   //Wait 3 seconds & close app
   setInterval(Exit, 3000);
}

function Exit()
{
   //Close app
   app.Exit();
}

function VerifyAnticrack(){
    //Check Root permission(su) &  Hacker softwares
    for(var i in apps){
      var a = apps[i];
      l = a.label;
      p = a.packageName;
      c = a.className;

      if(lucky==true){
         Alert();
         break;
      }
      else if(jasi== true){
         Alert();
         break;
      }
      else if(supersu == true){
         Alert();
         break;
      }
      else if(magisk== true){
         Alert();
         break;
      }
      else if(xposed== true){
         Alert();
         break;
      }
      else {
        app.ShowProgressBar("Procurando Apps Maliciosos...");
        for( prog = 0; prog < 100; prog++ ){
            app.UpdateProgressBar( prog );
        }
        app.HideProgressBar();
        app.ShowPopup("Nenhum app malicioso encontrado!", "Short");
        break;
      }
    }
}

function OnStart() {
    
    VerifyAnticrack();
    
    // Verifica se a permissão já foi concedida
    if (app.CheckPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
        app.ShowPopup("Permissões já concedidas!", "Short");
    } else {
        // Solicita permissão ao usuário
        app.GetPermission("android.permission.WRITE_EXTERNAL_STORAGE", PermissionResult);
    }

    // Callback para tratar o resultado da solicitação de permissão
    function PermissionResult(granted) {
        if (granted) {
            app.ShowPopup("Permissão concedida com sucesso!", "Short");
        } else {
            app.ShowPopup("Permissão não concedida!", "Short");
        }
    }
    
    // Criar layout principal
    let layout = app.CreateLayout("linear", "VCenter,FillXY");
    let colorPattern = "#1E1E2E";
    layout.SetBackColor(colorPattern);

    // Criar uma barra superior estilizada para o Título do app
    let topBar1 = app.CreateLayout("linear", "Horizontal");
    topBar1.SetBackColor(colorPattern);
    topBar1.SetSize(1, 0.08);

    // Criar o texto do título com estilo centralizado
    let Title = app.CreateText("", 1, 0.1, "Bold");
    Title.SetTextColor("#FFFFFF");
    Title.SetTextSize(45); // Fonte grande
    Title.SetPadding(0, 0, 0, 0); // Centraliza verticalmente no layout
    topBar1.AddChild(Title);

    // Adicionar a barra superior ao layout principal
    layout.AddChild(topBar1);

    // Criar uma barra superior estilizada para botões
    let topBar2 = app.CreateLayout("linear", "Horizontal");
    topBar2.SetBackColor(colorPattern);
    topBar2.SetSize(1, 0.07);

    // Botao importar script
    let btnSelectFile = app.CreateButton("[fa-upload]", 0.2, 0.05, "FontAwesome");
    btnSelectFile.SetBackColor("#444466");
    btnSelectFile.SetTextColor("#FFFFFF");
    btnSelectFile.SetOnTouch(() => {
        selectShellScript();
    });
    topBar2.AddChild(btnSelectFile);

    // Botao Limpar terminal
    let btnClean = app.CreateButton("[fa-eraser]", 0.2, 0.05, "FontAwesome");
    btnClean.SetBackColor("#444466");
    btnClean.SetTextColor("#FFFFFF");
    btnClean.SetOnTouch(() => {
        CleanTerminal(); // Chama a função para limpar o terminal
    });
    topBar2.AddChild(btnClean);

    layout.AddChild(topBar2);

    // Botao Editor Shell
    let btnEditor = app.CreateButton("[fa-pencil]", 0.2, 0.05, "FontAwesome");
    btnEditor.SetBackColor("#444466");
    btnEditor.SetTextColor("#FFFFFF");
    btnEditor.SetOnTouch(() => {
        ShellEdit(); // Chama a função para abrir o editor
    });
    topBar2.AddChild(btnEditor);

    layout.AddChild(topBar2);

    // Botao Github
    let btnGithub = app.CreateButton("[fa-github]", 0.2, 0.05, "FontAwesome");
    btnGithub.SetBackColor("#444466");
    btnGithub.SetTextColor("#FFFFFF");
    btnGithub.SetOnTouch(() => {
        Github(); // Chama a função para acessar o GitHub
    });
    topBar2.AddChild(btnGithub);

    layout.AddChild(topBar2);

    // Campo de entrada para comandos Shell
    let inputCommand = app.CreateTextEdit("", 0.9, 0.04);
    inputCommand.SetHint("shell@onow:~$");
    inputCommand.SetBackColor("#2A2D40");
    inputCommand.SetTextColor("#FFFFFF");
    inputCommand.SetCursorColor("#FFAA33");
    layout.AddChild(inputCommand);

    // Botão para executar o comando
    let btnExecute = app.CreateButton("[fa-play]", 0.4, 0.06, "FontAwesome");
    btnExecute.SetBackColor("#444466");
    btnExecute.SetTextColor("#FFFFFF");
    btnExecute.SetOnTouch(() => {
        let command = inputCommand.GetText();
        if (command.trim() !== "") {
            executeShellCommand(command);
        } else {
            app.ShowPopup("\u26a0\ufe0f Por favor, insira um comando.", "Short");
        }
    });
    layout.AddChild(btnExecute);

    // Campo para exibir a saída do comando
    let output = app.CreateTextEdit("", 0.9, 0.3, "ReadOnly,MultiLine");
    output.SetBackColor("#1A1A1E");
    output.SetTextColor("#00FF7F");
    output.SetCursorColor("#00FF7F");
    output.SetHint("Saída do comando será exibida aqui");
    layout.AddChild(output);

    // Campo para visualizar o conteúdo do arquivo .sh
    let fileContent = app.CreateTextEdit("", 0.9, 0.3, "ReadOnly");
    fileContent.SetBackColor("#2A2D40");
    fileContent.SetTextColor("#FFFFFF");
    fileContent.SetCursorColor("#FFAA33");
    fileContent.SetHint("Conteúdo do arquivo shell será exibido aqui");
    layout.AddChild(fileContent);

    // Botão para executar o arquivo .sh
    let btnRunFile = app.CreateButton("[fa-terminal]", 0.4, 0.06, "FontAwesome");
    btnRunFile.SetBackColor("#444466");
    btnRunFile.SetTextColor("#FFFFFF");
    btnRunFile.SetOnTouch(() => {
        let commands = fileContent.GetText();
        if (commands.trim() !== "") {
            executeShellCommand(commands);
        } else {
            app.ShowPopup("\u26a0\ufe0f Nenhum conteúdo para executar.", "Short");
        }
    });
    layout.AddChild(btnRunFile);

    // Adicionar rodapé com versão do app
    let footer = app.CreateText("", 0.9, 0.03, "Center");
    footer.SetBackColor(colorPattern);
    footer.SetTextColor("#888888");
    footer.SetTextSize(12);

    // Ler e exibir versão do arquivo JSON
    try {
        let jsonData = app.ReadFile("shellonow.json");
        let appData = JSON.parse(jsonData);
        Title.SetText(appData.appName);
        footer.SetText(`v${appData.version}`);
    } catch (e) {
        footer.SetText("Erro ao carregar versão do aplicativo.");
    }

    layout.AddChild(footer);

    // Adicionar layout à tela
    app.AddLayout(layout);

    // Função para executar comandos Shell
    function executeShellCommand(command) {
        app.ShowPopup("\u23f3 Executando comando...");

        try {
            // Executar o comando via app.SysExec()
            let result = app.SysExec(command);
            if (result) {
                output.SetText(result);
            } else {
                output.SetText("\u26a0\ufe0f Erro ao executar comando ou saída vazia.");
            }
        } catch (e) {
            output.SetText("\u274c Erro: " + e.message);
        }
    }

    // Função para acessar projeto open-source
    function Github() {
        app.OpenUrl("https://github.com/ON00dev/shellonow");
    }

    // Função para baixar Editor Shell Script
    function ShellEdit() {
        app.OpenUrl("https://play.google.com/store/apps/details?id=com.qamar.editor.shellscript");
    }

    // Função para limpar o terminal
    function CleanTerminal() {
        output.SetText("");
        inputCommand.SetText("");
        app.ShowPopup("\ud83c\udf4a Terminal limpo.", "Short");
    }

    // Função para selecionar e carregar o arquivo .sh
    function selectShellScript() {
        // Abre o seletor de arquivos sem filtro
        app.ChooseFile("Selecione um arquivo", null, onSelect);
    }
    
    // Callback ao selecionar o arquivo
    function onSelect(fileUri) {
        if (fileUri) {
            // Verifica se o arquivo possui a extensão .sh
            if (fileUri.toLowerCase().endsWith(".sh")) {
                app.ShowPopup("📁 Arquivo selecionado: " + fileUri);
                
                // Ler o conteúdo do arquivo
                let content = app.ReadFile(fileUri);
                if (content) {
                    fileContent.SetText(content);
                } else {
                    app.ShowPopup("⚠️ Erro ao ler o arquivo.");
                }
            } else {
                app.ShowPopup("⚠️ O arquivo selecionado não é um .sh.");
            }
        } else {
            app.ShowPopup("⚠️ Nenhum arquivo selecionado.");
        }
    }
}