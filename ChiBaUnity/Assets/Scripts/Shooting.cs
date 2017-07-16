using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Shooting : MonoBehaviour {

    public InventoryItem computer;
    public InventoryItem sunglasses;
    public InventoryItem umbrella;
    public InventoryItem readingglasses;

    private Inventory inventory;


    public void Shoot()
    {
        inventory = GameObject.Find("Player").GetComponent<Inventory>();

        Dictionary<InventoryItem, int> currentItems = inventory.items;

        if (currentItems.ContainsKey(computer))
        {
            GameObject obj = Instantiate(computer.prefab, transform.position, transform.rotation);
            obj.transform.position = new Vector3(Random.Range(-200.0f, 200.0f), 700, Random.Range(-200.0f, 200.0f));

            inventory.RemoveItem(computer);
        }

        else if (currentItems.ContainsKey(sunglasses))
        {
            GameObject obj = Instantiate(sunglasses.prefab, transform.position, transform.rotation);
            obj.transform.position = new Vector3(Random.Range(-200.0f, 200.0f), 700, Random.Range(-200.0f, 200.0f));

            inventory.RemoveItem(sunglasses);
        }

        else if (currentItems.ContainsKey(umbrella))
        {
            GameObject obj = Instantiate(umbrella.prefab, transform.position, transform.rotation);
            obj.transform.position = new Vector3(Random.Range(-200.0f, 200.0f), 700, Random.Range(-200.0f, 200.0f));

            inventory.RemoveItem(umbrella);
        }

        else if (currentItems.ContainsKey(readingglasses))
        {
            GameObject obj = Instantiate(readingglasses.prefab, transform.position, transform.rotation);
            obj.transform.position = new Vector3(Random.Range(-200.0f, 200.0f), 700, Random.Range(-200.0f, 200.0f));

            inventory.RemoveItem(readingglasses);
        }
    }
}
