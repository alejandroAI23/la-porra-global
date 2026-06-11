import { Routes, Route, Link, useNavigate, Navigate } from 'react-router-dom'
import { useState } from 'react'
import Home from './pages/Home.jsx'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import Matches from './pages/Matches.jsx'
import Leagues from './pages/Leagues.jsx'
import LeagueRanking from './pages/LeagueRanking.jsx'
import Bars from './pages/Bars.jsx'
import Profile from './pages/Profile.jsx'
import { isAuthenticated, logout } from './api/client.js'

function RequireAuth({ children }) {
  return isAuthenticated() ? children : <Navigate to="/login" replace />
}

export default function App() {
  const navigate = useNavigate()
  const [authed, setAuthed] = useState(isAuthenticated())

  function handleLogout() {
    logout()
    setAuthed(false)
    navigate('/')
  }

  return (
    <div className="min-h-screen bg-slate-900 text-slate-100">
      <nav className="border-b border-slate-700 bg-slate-800/60 px-6 py-4">
        <div className="mx-auto flex max-w-5xl flex-wrap items-center justify-between gap-4">
          <Link to="/" className="text-xl font-bold text-pitch-500">
            ⚽ La Porra Global
          </Link>
          <div className="flex flex-wrap items-center gap-4 text-sm">
            <Link to="/partidos" className="hover:text-pitch-500">
              Partidos
            </Link>
            <Link to="/ligas" className="hover:text-pitch-500">
              Ligas
            </Link>
            <Link to="/bares" className="hover:text-pitch-500">
              Bares
            </Link>
            {authed ? (
              <>
                <Link to="/perfil" className="hover:text-pitch-500">
                  Mi perfil
                </Link>
                <button
                  onClick={handleLogout}
                  className="rounded-lg border border-slate-600 px-3 py-1.5 hover:border-pitch-500"
                >
                  Salir
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="hover:text-pitch-500">
                  Entrar
                </Link>
                <Link
                  to="/register"
                  className="rounded-lg bg-pitch-700 px-3 py-1.5 font-medium hover:bg-pitch-500"
                >
                  Registrarse
                </Link>
              </>
            )}
          </div>
        </div>
      </nav>
      <main className="mx-auto max-w-5xl px-6 py-10">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login onAuth={() => setAuthed(true)} />} />
          <Route
            path="/register"
            element={<Register onAuth={() => setAuthed(true)} />}
          />
          <Route path="/partidos" element={<Matches />} />
          <Route path="/bares" element={<Bars />} />
          <Route
            path="/ligas"
            element={
              <RequireAuth>
                <Leagues />
              </RequireAuth>
            }
          />
          <Route
            path="/ligas/:id"
            element={
              <RequireAuth>
                <LeagueRanking />
              </RequireAuth>
            }
          />
          <Route
            path="/perfil"
            element={
              <RequireAuth>
                <Profile />
              </RequireAuth>
            }
          />
        </Routes>
      </main>
      <footer className="border-t border-slate-800 px-6 py-6 text-center text-xs text-slate-500">
        Plataforma no oficial e independiente. Sin apuestas con dinero real:
        solo predicciones lúdicas y rankings privados.
      </footer>
    </div>
  )
}
