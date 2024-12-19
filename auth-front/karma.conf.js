module.exports = function (config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine', '@angular-devkit/build-angular'],
    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-spec-reporter'),
      require('@angular-devkit/build-angular/plugins/karma'),
    ],
    client: {
      clearContext: false, // Leave Jasmine Spec Runner output visible in browser
    },
    reporters: ['spec'], // Spec reporter for detailed test names
    specReporter: {
      suppressPassed: false, // Show passed tests
      suppressFailed: false, // Show failed tests
      suppressSkipped: true, // Don't show skipped tests
    },
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    browsers: ['ChromeHeadless'],
    singleRun: false,
    restartOnFileChange: true,
  });
};
