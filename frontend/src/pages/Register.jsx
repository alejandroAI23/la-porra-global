import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api, setToken } from '../api/client.js'

export default function Register({ onAuth }) {
  const navigate = useNavigate()
  const [form, setForm] = useState({
    username: '',
    email: '',
    password: '',
    displayName: '',
  })
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const res = await api.register(form)
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
      <h1 className="text-2xl font-bold">Crear cuenta</h1>
      <form onSubmit={handleSubmit} className="mt-6 space-y-4">
        <input
          className="w-full rounded-lg border border-slate-700 bg-slate-800 px-4 py-2"
          placeholder="Usuario"
          value={form.username}
          onChange={(e) => setForm({ ...form, username: e.target.value })}
          required
          minLength={3}
        />
        <input
          type="email"
          className="w-full rounded-lg border border-slate-700 bg-slate-800 px-4 py-2"
          placeholder="Email"
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
          required
        />
        <input
          className="w-full rounded-lg border border-slate-700 bg-slate-800 px-4 py-2"
          placeholder="Nombre visible (opcional)"
          value={form.displayName}
          onChange={(e) => setForm({ ...form, displayName: e.target.value })}
        />
        <input
          type="password"
          className="w-full rounded-lg border border-slate-700 bg-slate-800 px-4 py-2"
          placeholder="Contraseña (mín. 8 caracteres)"
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
          required
          minLength={8}
        />
        {error && <p className="text-sm text-red-400">{error}</p>}
        <button
          disabled={loading}
          className="w-full rounded-lg bg-pitch-700 py-2 font-medium hover:bg-pitch-500 disabled:opacity-50"
        >
          {loading ? 'Creando…' : 'Registrarse'}
        </button>
      </form>
    </div>
  )
}
