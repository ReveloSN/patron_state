import { useState, type FormEvent } from "react";
import type { CreateReactionPayload } from "../types/api";

interface CreateReactionFormProps {
  pending: boolean;
  onSubmit: (payload: CreateReactionPayload) => Promise<void>;
}

export function CreateReactionForm({ pending, onSubmit }: CreateReactionFormProps) {
  const [name, setName] = useState("Esterification");
  const [catalyst, setCatalyst] = useState("Sulfuric Acid");
  const [temperatureCelsius, setTemperatureCelsius] = useState("25");
  const [pressureAtm, setPressureAtm] = useState("1");
  const [productConcentrationPercentage, setProductConcentrationPercentage] = useState("0");

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    await onSubmit({
      name,
      catalyst,
      temperatureCelsius: Number(temperatureCelsius),
      pressureAtm: Number(pressureAtm),
      productConcentrationPercentage: Number(productConcentrationPercentage),
    });
  }

  return (
    <section className="panel-section">
      <div className="section-heading">
        <h3>Create reaction</h3>
        <p>Define the initial reaction identity and prepared conditions before adding reactants.</p>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="field field--full">
            <label htmlFor="reaction-name">Reaction name</label>
            <input
              id="reaction-name"
              value={name}
              onChange={(event) => setName(event.target.value)}
              placeholder="Reaction name"
              required
            />
          </div>

          <div className="field field--full">
            <label htmlFor="reaction-catalyst">Catalyst</label>
            <input
              id="reaction-catalyst"
              value={catalyst}
              onChange={(event) => setCatalyst(event.target.value)}
              placeholder="Optional catalyst"
            />
          </div>

          <div className="field">
            <label htmlFor="reaction-temperature">Temperature (deg C)</label>
            <input
              id="reaction-temperature"
              type="number"
              step="0.1"
              value={temperatureCelsius}
              onChange={(event) => setTemperatureCelsius(event.target.value)}
              required
            />
          </div>

          <div className="field">
            <label htmlFor="reaction-pressure">Pressure (atm)</label>
            <input
              id="reaction-pressure"
              type="number"
              step="0.1"
              min="0"
              value={pressureAtm}
              onChange={(event) => setPressureAtm(event.target.value)}
              required
            />
          </div>

          <div className="field field--full">
            <label htmlFor="reaction-concentration">Initial concentration (%)</label>
            <input
              id="reaction-concentration"
              type="number"
              step="0.1"
              min="0"
              max="100"
              value={productConcentrationPercentage}
              onChange={(event) => setProductConcentrationPercentage(event.target.value)}
              required
            />
          </div>
        </div>

        <div className="action-row">
          <button className="button button--primary" type="submit" disabled={pending}>
            {pending ? "Creating..." : "Create prepared reaction"}
          </button>
        </div>
      </form>
    </section>
  );
}
