import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api, setToken } from '../api/client.js'

export default function Login({ onAuth }) {
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const res = await api.login(form)
      setToken(res.token)
      onAuth?.()
      navigate('/partidos')
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="mx-auto max-w-sm">
      <h1 className="text-2xl font-bold">Iniciar sesión</h1>
      <form onSubmit={handleSubmit} className="mt-6 space-y-4">
        <input
          className="w-full rounded-lg border border-slate-700 bg-slate-800 px-4 py-2"
          placeholder="Usuario"
          value={form.username}
          onChange={(e) => setForm({ ...form, username: e.target.value })}
          required
        />
        <input
          type="password"
          className="w-full rounded-lg border border-slate-700 bg-slate-800 px-4 py-2"
          placeholder="Contraseña"
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
          required
        />
        {error && <p className="text-sm text-red-400">{error}</p>}
        <button
          disabled={loading}
          className="w-full rounded-lg bg-pitch-700 py-2 font-medium hover:bg-pitch-500 disabled:opacity-50"
        >
          {loading ? 'Entrando…' : 'Entrar'}
        </button>
      </form>
    </div>
  )
}
