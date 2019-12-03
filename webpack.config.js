const path = require('path');


const styleEntryPoint = 'main';

module.exports = [{
  mode: 'development',
  entry: './resources/scss/' + styleEntryPoint + '.scss',
  output: {
    path : path.resolve(__dirname, './resources/public'),
    // This is necessary for webpack to compile
    // But we never use style-bundle.js
    filename: './js/style-bundle.js',
  },
  module: {
    rules: [
      {
        test: /\.scss$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: './css/main.css',
            },
          },
          { loader: 'extract-loader' },
          { loader: 'css-loader' },
          { loader: 'sass-loader',
            options: {
              sassOptions: {
              includePaths: ['./node_modules']      }
            } }
        ]
      }
    ]
  },
}];
