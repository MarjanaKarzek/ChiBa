using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Shooting : MonoBehaviour {

    public InventoryItem projectileInventoryItem;
    private Inventory inventory;

    public GameObject prefabObject;

    public void Shoot()
    {
        inventory = transform.parent.GetComponent<Inventory>();

        if (inventory.RemoveItem(projectileInventoryItem))
        {
            GameObject obj = Instantiate(projectileInventoryItem.prefab, transform.position, transform. rotation);
            obj.transform.position = new Vector3(Random.Range(-200.0f, 200.0f), 700, Random.Range(-200.0f, 200.0f));
        }
    }
}
