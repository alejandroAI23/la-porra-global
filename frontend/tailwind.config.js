/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        pitch: {
          50: '#f0fdf4',
          500: '#16a34a',
          700: '#15803d',
          900: '#14532d',
        },
      },
    },
  },
  plugins: [],
}
