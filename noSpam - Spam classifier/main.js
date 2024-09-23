const { app, BrowserWindow } = require('electron');

function createWindow() {
    const win = new BrowserWindow({
        width: 1200,
        height: 700,
        frame: false,
        webPreferences: {
            nodeIntegration: true,
        },
    });

    win.loadFile('frontend/index.html');
}

function createWindow2(){
    loadingwin = new BrowserWindow({width: 1200, height: 700, frame: false});
    loadingwin.loadFile('frontend/LoadIndex.html');

    setTimeout(() =>{
        loadingwin.close();
        createWindow();
    }, 7000);
}

app.whenReady().then(createWindow2);

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
});

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
        createWindow();
    }
});
