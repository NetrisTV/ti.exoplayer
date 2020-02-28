//  Add the module as a dependency to your application by adding a `<module>`
//  item to the `<modules>` element of your `tiapp.xml` file:
//    <ti:app>
//      ...
//      <modules>
//        <module platform="android">ru.netris.mobile.exoplayer</module>
//      </modules>
//      ...
//    </ti:app>

const ExoPlayer = require('ru.netris.mobile.exoplayer');
const exolist = 'https://raw.githubusercontent.com/google/ExoPlayer/r2.9.6/' +
    'demos/main/src/main/assets/media.exolist.json';

const OPTIONS = {
  'Use Ti.Media.VideoPlayer': true,
  fullscreen: false,
  autoplay: true,
  repeatMode: false,
  showsControls: true
};

const PlayerEvents = [
  'click',
  'complete',
  'durationavailable',
  'error',
  'load',
  'loadstate',
  'metadata',
  'playbackstate',
  'playing',
  'postlayout',
  'preload',
  'naturalsizeavailable',
  'trackschange',
  'volumechange'
];

/**
 * @param {{
 *   drmScheme: (string | undefined),
 *   drmLicenseUrl: (string | undefined),
 *   drmKeyRequestProperties: (string | undefined),
 *   adTagUri: (string | undefined),
 *   contentType: (number | undefined),
 *   default: boolean,
 *   fullscreen: boolean,
 *   autoplay: boolean,
 *   showsControls: boolean,
 *   repeatMode: number,
 *   url: string
 * }} options
 */
function play(options) {
  const opts = {};

  Object.keys(options).forEach(function(key) {
    opts[key] = options[key];
  });
  console.log('Create player with ' + JSON.stringify(opts));
  const player = options.default ? Ti.Media.createVideoPlayer(opts) : ExoPlayer.createVideoPlayer(opts);

  PlayerEvents.forEach(function(event) {
    player.addEventListener(event, function(e) {
      console.log('PLAYER EVENT ' + event);
      try {
        delete e.source;
        console.log(e);
      } catch (e) {}
    });
  });

  if (!options.fullscreen) {
    opts['height'] = 300;
    opts['width'] = 300;
    opts['top'] = 10;
    createWindowAndControls(options['name'], player)
  }
}

function createWindowAndControls(name, player) {
  const vidWin = Ti.UI.createWindow({
    title: name || 'Video View Demo',
    backgroundColor: '#fff'
  });
  const controls = Ti.UI.createView({
    layout: 'vertical',
    width: Ti.UI.FILL,
    height: Ti.UI.SIZE,
    bottom: 0,
  });
  const buttons = Ti.UI.createView({
    layout: 'horizontal',
    width: Ti.UI.FILL,
    height: Ti.UI.SIZE
  });
  const buttonIncrease = Ti.UI.createButton({
    title: '+',
    backgroundColor: '#ffbbbb',
    color: '#000',
    width: '33%'
  });
  const buttonReset = Ti.UI.createButton({
    title: 'reset',
    backgroundColor: '#bbbbbb',
    color: '#000',
    width: '34%'
  });
  const buttonReduce = Ti.UI.createButton({
    title: '-',
    backgroundColor: '#bbffbb',
    color: '#000',
    width: '33%'
  });
  const label = Ti.UI.createLabel({
    text: JSON.stringify(player.playbackParameters),
    color: '#000',
    height: Ti.UI.SIZE,
    width: Ti.UI.FILL
  });
  const increaseSpeed = function() {
    const playbackParameters = player.playbackParameters;
    player.playbackParameters = {
      speed: playbackParameters.speed + 0.2
    };
    label.text = JSON.stringify(player.playbackParameters);
  };
  const resetParameters = function() {
    player.playbackParameters = ExoPlayer.DEFAULT_PLAYBACK_PARAMETERS;
    label.text = JSON.stringify(player.playbackParameters);
  };
  const reduceSpeed = function() {
    const playbackParameters = player.playbackParameters;
    const speed = playbackParameters.speed - 0.2;
    if (speed > 0) {
      player.playbackParameters = {
        speed: speed
      };
    }
    label.text = JSON.stringify(player.playbackParameters);
  };
  const removeListeners = function() {
    buttonIncrease.removeEventListener('click', increaseSpeed);
    buttonReset.removeEventListener('click', resetParameters);
    buttonReduce.removeEventListener('click', reduceSpeed);
    vidWin.removeEventListener('close', removeListeners);
  };

  buttonIncrease.addEventListener('click', increaseSpeed);
  buttonReset.addEventListener('click', resetParameters);
  buttonReduce.addEventListener('click', reduceSpeed);
  vidWin.addEventListener('close', removeListeners);

  buttons.add(buttonIncrease);
  buttons.add(buttonReset);
  buttons.add(buttonReduce);
  controls.add(label);
  controls.add(buttons);
  vidWin.add(controls);
  vidWin.add(player);
  vidWin.open();
}


function convertProperty(key, value) {
  switch (key) {
  case 'ad_tag_uri':
    return {key: 'adTagUri', value: value};
  case 'drm_license_url':
    return {key: 'drmLicenseUrl', value: value};
  case 'drm_multi_session':
    return {key: 'drmMultiSession', value: value};
  case 'drm_key_request_properties':
    return {key: 'drmKeyRequestProperties', value: value};
  case 'drm_scheme':
    return {key: 'drmScheme', value: value};
  case 'extension':
    return {key: 'contentExtension', value: value};
  case 'uri':
    return {key: 'url', value: value};
  default:
    return {key: key, value: value};
  }
}

function loadList() {
  const xhr = Ti.Network.createHTTPClient({
    onload: function() {
      indicator.hide();
      createViews(JSON.parse(this.responseText));
    },
    onerror: function(e) {
      indicator.hide();
      const notification = Ti.UI.createNotification({
        message: e.code + ' ' + e.error,
        duration: Ti.UI.NOTIFICATION_DURATION_LONG
      });
      notification.show();
      console.error(e.code);
      console.error(e.error);
    }
  });

  xhr.open('GET', exolist);
  xhr.send();
}

function createViews(json) {
  const table = createTable(json);
  const view = Ti.UI.createView({
    layout: 'vertical',
    height: Ti.UI.FILL,
    width: Ti.UI.FILL
  });
  Object.keys(OPTIONS).forEach(function(name) {
    createSwitch(name, view);
  });
  view.add(table);
  win.add(view);
}

function createTable(json) {
  const data = [];
  json.forEach(function(o) {
    const section = Ti.UI.createTableViewSection({ headerTitle: o['name'] });
    o['samples'].forEach(function(row) {
      section.add(Ti.UI.createTableViewRow({
        color: '#000',
        title: row['name'],
        data: row
      }));
    });
    data.push(section);
  });

  const table = Ti.UI.createTableView({data: data});
  table.addEventListener('click', function click(e) {
    if (e && e['source'] && e['source']['data'] && e['source']['data']['uri']) {
      const options = e['source']['data'];
      const opts = {
        url: '',
        default: !!OPTIONS['Use Ti.Media.VideoPlayer'],
        fullscreen: !!OPTIONS.fullscreen,
        autoplay: !!OPTIONS.autoplay,
        repeatMode: OPTIONS.repeatMode ?
          Ti.Media.VIDEO_REPEAT_MODE_ONE : Ti.Media.VIDEO_REPEAT_MODE_NONE,
        showsControls: !!OPTIONS.showsControls
      };
      Object.keys(options).forEach(function(key) {
        const temp = convertProperty(key, options[key]);
        opts[temp.key] = temp.value;
      });
      play(opts);
    }
  });
  return table;
}

function createSwitch(name, parent) {
  const line = Ti.UI.createView({
    backgroundColor: '#eee',
    height: Ti.UI.SIZE,
    width: Ti.UI.FILL
  });
  const basicSwitch = Ti.UI.createSwitch({
    right: 10,
    value: OPTIONS[name]
  });

  basicSwitch.addEventListener('change', function(e) {
    OPTIONS[name] = e.value;
    label1.color = OPTIONS[name] ? '#900' : '#666';
  });
  const label1 = Ti.UI.createLabel({
    color: OPTIONS[name] ? '#900' : '#666',
    text: name,
    textAlign: Ti.UI.TEXT_ALIGNMENT_LEFT,
    left: 10,
    width: Ti.UI.SIZE,
    height: Ti.UI.SIZE
  });

  line.add(label1);
  line.add(basicSwitch);
  parent.add(line);
}


const win = Ti.UI.createWindow({
  title: 'Ti ExoPlayer Demo',
  backgroundColor: '#fff'
});

const indicator = Ti.UI.createActivityIndicator();
win.add(indicator);
win.addEventListener('open', function() {
  indicator.show();
  loadList();
});

win.open();
